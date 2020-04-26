package ir.kindnesswall.view.profile.blocklist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.databinding.ActivityBlockListBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import ir.kindnesswall.view.profile.bookmarks.UserBookmarksAdapter
import org.koin.android.viewmodel.ext.android.viewModel

class BlockListActivity : BaseActivity(), OnItemClickListener {

    private val viewModel: BlockListViewModel by viewModel()

    lateinit var binding: ActivityBlockListBinding

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, BlockListActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_block_list)

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

    private fun getBlockedUsers() {
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
