package com.farshidabz.kindnesswall.view.profile.blocklist

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.databinding.ActivityBlockListBinding
import com.farshidabz.kindnesswall.databinding.ActivityBookmarksBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener
import com.farshidabz.kindnesswall.view.giftdetail.GiftDetailActivity
import com.farshidabz.kindnesswall.view.profile.bookmarks.UserBookmarksAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class BlockListActivity : BaseActivity(), OnItemClickListener {

    private val viewModel: BlockListViewModel by viewModel()

    lateinit var binding: ActivityBlockListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookmarks)

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { onBackPressed() }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.blockedUsersRecyclerView.adapter = UserBookmarksAdapter(this)

        binding.blockedUsersRecyclerView.setHasFixedSize(true)
        binding.blockedUsersRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getBookmarks() {
//        viewModel.bookmarkList?.observe(this) {
//            when (it.status) {
//                CustomResult.Status.LOADING -> {
//                }
//                CustomResult.Status.SUCCESS -> {
//                    it.data?.let { regions ->
//                        (binding.bookmarksRecyclerView.adapter as UserBookmarksAdapter).submitList(regions)
//                    }
//                }
//
//                CustomResult.Status.ERROR -> {
//                }
//            }
//        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        GiftDetailActivity.start(this, obj as GiftModel)
    }
}
