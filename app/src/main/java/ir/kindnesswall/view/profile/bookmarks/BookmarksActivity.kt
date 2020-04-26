package ir.kindnesswall.view.profile.bookmarks

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityBookmarksBinding
import ir.kindnesswall.databinding.FragmentChooseRegionBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.view.citychooser.adapters.RegionListAdapter
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import org.koin.android.viewmodel.ext.android.viewModel

class BookmarksActivity : BaseActivity(), OnItemClickListener {

    private val viewModel: BookmarksViewModel by viewModel()

    lateinit var binding: ActivityBookmarksBinding

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
        binding.bookmarksRecyclerView.adapter = UserBookmarksAdapter(this)

        binding.bookmarksRecyclerView.setHasFixedSize(true)
        binding.bookmarksRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getBookmarks() {
        viewModel.bookmarkList?.observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    it.data?.let { regions ->
                        (binding.bookmarksRecyclerView.adapter as UserBookmarksAdapter).submitList(regions)
                    }
                }

                CustomResult.Status.ERROR -> {
                }
            }
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        GiftDetailActivity.start(this, obj as GiftModel)
    }
}
