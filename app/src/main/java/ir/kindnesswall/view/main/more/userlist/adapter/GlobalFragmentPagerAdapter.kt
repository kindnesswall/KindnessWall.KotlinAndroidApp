package ir.kindnesswall.view.main.more.userlist.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ir.kindnesswall.view.main.more.userlist.tabs.UserListFragment

/**
 * This class is responsible for providing Pager adapter according to 'mPage' input type
 */
class GlobalFragmentPagerAdapter(manager: FragmentManager, private val mCount: Int, private val mPage: Int) : FragmentPagerAdapter(manager) {
    override fun getItem(position: Int): Fragment {
        when (mPage) {
            PAGE_USER_LIST -> when(position){
                TAB_USER_LIST_ACTIVE ->return UserListFragment.start(TAB_USER_LIST_ACTIVE)
                TAB_USER_LIST_BLOCK -> return UserListFragment.start(TAB_USER_LIST_BLOCK)
//                TAB_USER_LIST_CHAT_BLOCK -> return UserListFragment.start(TAB_USER_LIST_CHAT_BLOCK)
            }
        }
        return Fragment()
    }

    override fun getCount(): Int {
        return mCount
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }

    companion object {
        // Page User List
        const val PAGE_USER_LIST = 0
        const val PAGE_USER_LIST_COUNT = 2

        const val TAB_USER_LIST_ACTIVE = 0
        const val TAB_USER_LIST_BLOCK = 1
//        const val TAB_USER_LIST_CHAT_BLOCK = 2

        // Other Const here
    }

}