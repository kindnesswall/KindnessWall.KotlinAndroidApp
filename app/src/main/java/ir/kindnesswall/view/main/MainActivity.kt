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
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.databinding.ActivityMainBinding
import ir.kindnesswall.utils.OnClickListener
import ir.kindnesswall.view.authentication.AuthenticationActivity
import ir.kindnesswall.view.main.addproduct.SubmitGiftActivity
import ir.kindnesswall.view.main.conversation.ConversationFragment
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainViewModel>()

    private var currentController: NavController? = null

    private val navHomeController: NavController by lazy { findNavController(R.id.homeTab) }
    private val navCharityController: NavController by lazy { findNavController(R.id.charityTab) }
    private val navConversationController: NavController by lazy { findNavController(R.id.conversationTab) }
    private val navMoreController: NavController by lazy { findNavController(R.id.moreTab) }

    private var onTabClickedListener: OnClickListener<Int>? = null

    private lateinit var binding: ActivityMainBinding

    companion object {
        fun start(context: Context?, defaultTab: Int = R.id.navigation_home) {
            context?.let {
                val intent = Intent(it, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .putExtra("defaultTab", defaultTab)

                it.startActivity(intent)
            }
        }
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

        binding.mainBottomNavigationView
            .setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        binding.mainBottomNavigationView.itemIconTintList = null

        if (savedInstanceState == null) {
            currentController = navHomeController
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        switchTab(binding.mainBottomNavigationView.selectedItemId)
    }

    override fun onResume() {
        super.onResume()

        updatePagesList()

        AppPref.isInChatPage = false
        AppPref.currentChatSessionId = -1
    }

    private fun updatePagesList() {
        getConversationFragment()?.loadData()
    }

    private fun getConversationFragment(): ConversationFragment? {
        val favoriteFragmentNavHost =
            supportFragmentManager.findFragmentById(R.id.conversationTab)
        favoriteFragmentNavHost?.childFragmentManager?.fragments?.filterNotNull()?.find {
            if (it is ConversationFragment) {
                return it
            }
            true
        }

        return null
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        currentController?.navigateUp()
    }

    override fun onBackPressed() {
        currentController?.let {
            if (it.popBackStack().not()) {
                if (currentController != navHomeController) {
                    switchTab(R.id.navigation_home)
                    binding.mainBottomNavigationView.menu.findItem(R.id.navigation_home)?.isChecked =
                        true
                } else {
                    return finish()
                }
            }
        } ?: run { finish() }
    }

    private fun fixBottomNavigationBug() {
        val menuView = binding.mainBottomNavigationView.getChildAt(0) as? ViewGroup ?: return

        menuView.forEach {
            it.findViewById<View>(R.id.largeLabel)?.setPadding(0, 0, 0, 0)
        }
    }

    private fun selectDefaultTab() {
        switchTab(viewModel.defaultTab)
        binding.mainBottomNavigationView.menu.findItem(viewModel.defaultTab)?.isChecked = true
    }

    private fun switchTab(tabId: Int): Boolean {
        when (tabId) {
            R.id.navigation_home -> {
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
                if (UserInfoPref.bearerToken.isEmpty()) {
                    AuthenticationActivity.start(this)
                } else {
                    SubmitGiftActivity.start(this)
                }
            }

            R.id.navigation_conversation -> {
                if (!UserInfoPref.bearerToken.isEmpty()) {
                    currentController = navConversationController

                    binding.conversationTabContainer.visibility = View.VISIBLE
                    binding.homeTabContainer.visibility = View.INVISIBLE
                    binding.charityTabContainer.visibility = View.INVISIBLE
                    binding.moreTabContainer.visibility = View.INVISIBLE
                } else {
                    AuthenticationActivity.start(this)
                }
            }

            R.id.navigation_more -> {
                currentController = navMoreController

                binding.moreTabContainer.visibility = View.VISIBLE
                binding.homeTabContainer.visibility = View.INVISIBLE
                binding.charityTabContainer.visibility = View.INVISIBLE
                binding.conversationTabContainer.visibility = View.INVISIBLE
            }
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
