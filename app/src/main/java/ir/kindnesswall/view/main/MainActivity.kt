package ir.kindnesswall.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.databinding.ActivityMainBinding
import ir.kindnesswall.utils.BottomTabHistory
import ir.kindnesswall.utils.OnClickListener
import ir.kindnesswall.view.main.addproduct.SubmitGiftActivity
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private var currentController: NavController? = null
    private var tabHistory = BottomTabHistory().apply { push(R.id.navigation_home) }

    private val navHomeController: NavController by lazy { findNavController(R.id.homeTab) }
    private val navCharityController: NavController by lazy { findNavController(R.id.charityTab) }
    private val navConversationController: NavController by lazy { findNavController(R.id.conversationTab) }
    private val navMoreController: NavController by lazy { findNavController(R.id.moreTab) }

    private var onTabClickedListener: OnClickListener<Int>? = null

    private lateinit var binding: ActivityMainBinding

    companion object {
        @JvmStatic
        fun start(context: Context?, defaultTab: Int = R.id.navigation_home) {
            context?.let {
                val intent = Intent(it, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra("defaultTab", defaultTab)
                it.startActivity(intent)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("TAB_HISTORY", tabHistory)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        savedInstanceState?.let {
            tabHistory = it.getSerializable("TAB_HISTORY") as BottomTabHistory
            switchTab(binding.mainBottomNavigationView.selectedItemId, false)
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.sendRegisterFirebaseToken()
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        currentController?.navigateUp()
    }

    override fun onBackPressed() {
        currentController?.let {
            if (it.popBackStack().not()) {
                if (tabHistory.size > 1) {
                    val tabId = tabHistory.popPrevious()
                    if (tabHistory.size == 1 && tabId != R.id.navigation_home)
                        tabHistory.push(R.id.navigation_home, false)
                    switchTab(tabId, false)
                    binding.mainBottomNavigationView.menu.findItem(tabId)?.isChecked = true
                } else {
                    tabHistory.clear()
                    return finish()
                }
            }
        } ?: run { finish() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        readIntent()
        configureViews(savedInstanceState)
    }

    private fun readIntent() {
        viewModel.defaultTab = intent.getIntExtra("defaultTab", R.id.navigation_home)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        fixBottomNavigationBug()
        selectDefaultTab()
        binding.mainBottomNavigationView.setOnNavigationItemSelectedListener(
            mOnNavigationItemSelectedListener
        )

        binding.mainBottomNavigationView.itemIconTintList = null

        if (savedInstanceState == null) {
            currentController = navHomeController
        }
    }

    private fun fixBottomNavigationBug() {
        val menuView = binding.mainBottomNavigationView.getChildAt(0) as? ViewGroup ?: return

        menuView.forEach {
            it.findViewById<View>(R.id.largeLabel)?.setPadding(0, 0, 0, 0)
        }
    }

    private fun selectDefaultTab() {
        tabHistory.push(viewModel.defaultTab)
        switchTab(viewModel.defaultTab, false)
        binding.mainBottomNavigationView.menu.findItem(viewModel.defaultTab)?.isChecked = true
    }

    private fun switchTab(tabId: Int, addToHistory: Boolean = true): Boolean {
        when (tabId) {
            R.id.navigation_home -> {
//                if (currentController == navIncomeController)
//                    sendBroadcast(Intent(HomeFragment.ACTION_HOME))

                currentController = navHomeController

                binding.homeTabContainer.visibility = View.VISIBLE
                binding.charityTabContainer.visibility = View.INVISIBLE
                binding.conversationTabContainer.visibility = View.INVISIBLE
                binding.moreTabContainer.visibility = View.INVISIBLE
            }

            R.id.navigation_charity -> {

                currentController = navCharityController

                binding.charityTabContainer.visibility = View.VISIBLE
                binding.homeTabContainer.visibility = View.INVISIBLE
                binding.conversationTabContainer.visibility = View.INVISIBLE
                binding.moreTabContainer.visibility = View.INVISIBLE
            }

            R.id.navigation_add_product -> {
                SubmitGiftActivity.start(this)
            }

            R.id.navigation_conversation -> {

                currentController = navConversationController

                binding.conversationTabContainer.visibility = View.VISIBLE
                binding.homeTabContainer.visibility = View.INVISIBLE
                binding.charityTabContainer.visibility = View.INVISIBLE
                binding.moreTabContainer.visibility = View.INVISIBLE
            }

            R.id.navigation_more -> {
//                if (currentController == navProfileController)
//                    sendBroadcast(Intent(ProfileFragment.ACTION_PROFILE))

                currentController = navMoreController

                binding.moreTabContainer.visibility = View.VISIBLE
                binding.homeTabContainer.visibility = View.INVISIBLE
                binding.charityTabContainer.visibility = View.INVISIBLE
                binding.conversationTabContainer.visibility = View.INVISIBLE
            }
        }
        if (addToHistory) {
            tabHistory.push(tabId)
        }

        return true
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            onTabClickedListener?.onClick(item.itemId)
            return@OnNavigationItemSelectedListener switchTab(item.itemId)
        }

    private fun showBadge(@IdRes itemId: Int, value: Int) {
        removeBadge(itemId)

        val itemView =
            binding.mainBottomNavigationView.findViewById<BottomNavigationItemView>(itemId)
        val badge = LayoutInflater.from(this)
            .inflate(
                R.layout.badge_bottom_navigation_layout,
                binding.mainBottomNavigationView,
                false
            )

        if (value <= 0) {
            badge.visibility = View.GONE
        } else {
            badge.visibility = View.VISIBLE
            val text = badge.findViewById<TextView>(R.id.bottomNavigationViewBadgeTextView)
            text.text = value.toString()
            itemView.addView(badge)
        }
    }

    private fun removeBadge(@IdRes itemId: Int) {
        val itemView =
            binding.mainBottomNavigationView.findViewById<BottomNavigationItemView>(itemId)
        if (itemView.childCount == 3) {
            itemView.removeViewAt(2)
        }
    }
}
