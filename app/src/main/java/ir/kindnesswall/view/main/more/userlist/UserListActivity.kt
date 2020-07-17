package ir.kindnesswall.view.main.more.userlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.utils.transformer.ZoomOutPageTransformer
import ir.kindnesswall.view.main.more.userlist.adapter.GlobalFragmentPagerAdapter
import ir.kindnesswall.view.main.more.userlist.adapter.GlobalFragmentPagerAdapter.Companion.PAGE_USER_LIST
import ir.kindnesswall.view.main.more.userlist.adapter.GlobalFragmentPagerAdapter.Companion.PAGE_USER_LIST_COUNT
import ir.kindnesswall.view.main.more.userlist.adapter.GlobalFragmentPagerAdapter.Companion.TAB_USER_LIST_ACTIVE
import ir.kindnesswall.view.main.more.userlist.adapter.GlobalFragmentPagerAdapter.Companion.TAB_USER_LIST_BLOCK
import kotlinx.android.synthetic.main.activity_about_us.backImageView
import kotlinx.android.synthetic.main.activity_user_list.*

class UserListActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, UserListActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        backImageView.setOnClickListener { onBackPressed() }
        initTab()
    }


    private fun initTab() {
        val mTabAdapter = GlobalFragmentPagerAdapter(supportFragmentManager, PAGE_USER_LIST_COUNT, PAGE_USER_LIST)
        vViewPager.adapter = mTabAdapter
        vViewPager.offscreenPageLimit = PAGE_USER_LIST_COUNT
        vViewPager.setPageTransformer(true, ZoomOutPageTransformer())
        vTab.setupWithViewPager(vViewPager)

        getTabTitle().also {
            it.text = getString(R.string.active)
            vTab.getTabAt(TAB_USER_LIST_ACTIVE)?.customView = it
        }

        getTabTitle().also {
            it.text = getString(R.string.blocked)
            vTab.getTabAt(TAB_USER_LIST_BLOCK)?.customView = it
        }

     /*   getTabTitle().also {
            it.text = getString(R.string.chat_blocked)
            vTab.getTabAt(TAB_USER_LIST_CHAT_BLOCK)?.customView = it
        }*/
        vTab.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.tab_txt_color))
        vViewPager.currentItem = TAB_USER_LIST_ACTIVE


    }
    private fun getTabTitle() : AppCompatTextView = LayoutInflater.from(this).inflate(R.layout.item_tab, null)
            .findViewById(R.id.item_tab_txt_title)
}
