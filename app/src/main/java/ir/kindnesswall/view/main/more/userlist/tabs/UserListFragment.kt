package ir.kindnesswall.view.main.more.userlist.tabs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.databinding.FragmentUserListBinding
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import ir.kindnesswall.view.main.more.userlist.IPeakUser
import ir.kindnesswall.view.main.more.userlist.adapter.UserListAdapter
import kotlinx.android.synthetic.main.fragment_user_list.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class UserListFragment : BaseFragment() {
    lateinit var mBinding: FragmentUserListBinding
    private val mViewModel: UserListViewModel by viewModel()
    private var mUserType by Delegates.notNull<Int>()
    private lateinit var mAdapter: UserListAdapter
    private lateinit var mEndless: EndlessRecyclerViewScrollListener
    private var mPeakUserListener: IPeakUser? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mPeakUserListener = context as IPeakUser
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        return mBinding.root
    }

    override fun configureViews() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        initRecyclerView()
        getUsers()
    }

    private fun initRecyclerView() {
        val animator = mBinding.rvUser.itemAnimator

        if (animator is SimpleItemAnimator)
            animator.supportsChangeAnimations = false

        mAdapter = UserListAdapter{ onUserClicked(it) }
        mAdapter.setHasStableIds(true)
        mBinding.rvUser.adapter = mAdapter
        mBinding.rvUser.setHasFixedSize(true)
        mBinding.rvUser.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        mBinding.swipeRefresh.setOnRefreshListener { refreshList() }
        setRecyclerViewPagination()

    }

    private fun onUserClicked(user: User){
        mPeakUserListener?.onUserPeaked(user)
    }

    private fun setRecyclerViewPagination() {
        mEndless = object : EndlessRecyclerViewScrollListener(mBinding.rvUser.layoutManager!!) {
            override fun onLoadMore() {
                mEndless.isLoading = true
                getUsers()
            }

            override fun onScrolled(position: Int) {
            }
        }

        mBinding.rvUser.addOnScrollListener(mEndless)
    }

    private fun refreshList() {
        mEndless.isLoading = false
        mBinding.swipeRefresh.isRefreshing = true
        mViewModel.mUserList.clear()
        getUsers()
    }

    private fun getUsers() {
        arguments?.getInt(KEY_TYPE)?.let {
            mUserType = it

            mViewModel.getUsers(mUserType)?.observe(this) { res ->
                mBinding.swipeRefresh.isRefreshing = false

                when (res.status) {
                    CustomResult.Status.SUCCESS -> {
                        res.data?.let { data ->
                            mViewModel.mUserList.addAll(data)
                            showList()
                            checkEmptyPage(data.isEmpty())
                        } ?: run {
                            checkEmptyPage(false)
                        }
                        pbCenter.visibility = View.GONE
                    }
//                    CustomResult.Status.LOADING -> emit(CustomResult.loading())
//                    else -> emit(CustomResult.error(result.errorMessage))
                }
            }
        }
    }

    private fun checkEmptyPage(isEmpty: Boolean) {
        // It means we are run out of page
        if (isEmpty && mEndless.isLoading) return

        if (!isEmpty && mEndless.isLoading)
            mEndless.isLoading = false
        else if (isEmpty)
            tvEmptyList.visibility = View.VISIBLE
        else
            tvEmptyList.visibility = View.GONE
    }

    private fun showList() {
        mAdapter.submitList(mViewModel.mUserList)
    }

    companion object {
        private const val KEY_TYPE = "user_page_type"

        fun start(type: Int): UserListFragment {
            val userFrg = UserListFragment()
            userFrg.arguments = Bundle().apply { putInt(KEY_TYPE, type) }
            return userFrg
        }
    }
}