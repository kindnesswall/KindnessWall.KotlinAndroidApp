package ir.kindnesswall.view.reviewgift

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityReviewGiftsBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import ir.kindnesswall.view.main.catalog.cataloglist.CatalogAdapter
import org.koin.android.viewmodel.ext.android.viewModel


class ReviewGiftsActivity : BaseActivity(), OnItemClickListener {

    private val viewModel: ReviewGiftsViewModel by viewModel()

    lateinit var binding: ActivityReviewGiftsBinding

    lateinit var swipeController: SwipeController

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ReviewGiftsActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_gifts)

        configureViews(savedInstanceState)
        configureViewModel()

        if (viewModel.reviewItem.isNullOrEmpty()) {
            getGiftsFirstPage()
        } else {
            showList()
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {

        binding.backImageView.setOnClickListener { onBackPressed() }

        binding.pullToRefreshLayout.setOnRefreshListener {
            binding.pullToRefreshLayout.isRefreshing = true
            refreshList()
        }


        initRecyclerView()
    }

    private fun configureViewModel() {

    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.apply {
            adapter = CatalogAdapter(this@ReviewGiftsActivity)
            setHasFixedSize(true)
            setRecyclerViewPagination(this.layoutManager as LinearLayoutManager)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        swipeController = SwipeController(this, object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                Log.e(">>>>>>", "on right side clicked $position")
                viewModel.reviewItem.removeAt(position)
                (binding.itemsListRecyclerView.adapter as CatalogAdapter).notifyItemRemoved(position)
            }

            override fun onLeftClicked(position: Int) {
                Log.e(">>>>>>", "on left side clicked $position")
                showGetInputDialog(Bundle().apply {
                    putString("title", getString(R.string.Please_write_reason))
                    putString("hint", getString(R.string.reason_of_reject))
                }, approveListener = {
                    viewModel.reviewItem.removeAt(position)
                    (binding.itemsListRecyclerView.adapter as CatalogAdapter).notifyItemRemoved(position)
                })
            }
        })

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(binding.itemsListRecyclerView)

        binding.itemsListRecyclerView.addItemDecoration(object : ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                swipeController.onDraw(c)
            }
        })
    }

    private fun setRecyclerViewPagination(layoutManager: LinearLayoutManager) {
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore() {
                    endlessRecyclerViewScrollListener.isLoading = true
                    loadNextPage()
                }

                override fun onScrolled(position: Int) {
                }
            }

        binding.itemsListRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun refreshList() {
        viewModel.reviewItem.clear()
        showList()
        getGiftsFirstPage()
    }

    private fun loadNextPage() {
        viewModel.getReviewItemsFromServer().observe(this) {
            onCatalogItemsReceived(it)
        }
    }

    private fun getGiftsFirstPage() {
        viewModel.getReviewItemsFirstPage().observe(this) {
            onCatalogItemsReceived(it)
        }
    }

    private fun onCatalogItemsReceived(it: CustomResult<List<GiftModel>>) {
        when (it.status) {
            CustomResult.Status.LOADING -> {
                showProgressDialog()
            }

            CustomResult.Status.SUCCESS -> {
                binding.pullToRefreshLayout.isRefreshing = false
                dismissProgressDialog()
                it.data?.let { data ->
                    viewModel.reviewItem.addAll(data)
                    showList()
                }
            }

            CustomResult.Status.ERROR -> {
                showToastMessage("")
            }
        }
    }

    private fun showList() {
        (binding.itemsListRecyclerView.adapter as CatalogAdapter).submitList(viewModel.reviewItem)
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        GiftDetailActivity.start(this, obj as GiftModel)
    }
}
