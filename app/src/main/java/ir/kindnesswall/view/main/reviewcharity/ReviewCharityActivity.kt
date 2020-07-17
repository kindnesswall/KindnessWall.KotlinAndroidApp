package ir.kindnesswall.view.main.reviewcharity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.ActivityReviewCharityBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import ir.kindnesswall.view.main.charity.CharityAdapter
import ir.kindnesswall.view.main.charity.charitydetail.CharityDetailActivity
import org.koin.android.viewmodel.ext.android.viewModel


class ReviewCharityActivity : BaseActivity(), OnItemClickListener {

    private val viewModel: ReviewCharityViewModel by viewModel()

    lateinit var binding: ActivityReviewCharityBinding

    lateinit var swipeController: SwipeController

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, ReviewCharityActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_review_charity)

        configureViews(savedInstanceState)
        configureViewModel()


/*        val data: MutableList<CharityModel> = ArrayList()
        data.add(CharityModel(0, 1, "name", false))
        viewModel.reviewItem.addAll(data)*/
        showList()
        checkEmptyState()

        if (viewModel.reviewItem.isNullOrEmpty()) {
            getGiftsFirstPage()
        } else {
            showList()
        }
    }

    override fun configureViews(savedInstanceState: Bundle?) {

        binding.backImageView.setOnClickListener { onBackPressed() }

        binding.pullToRefreshLayout.setOnRefreshListener {
            endlessRecyclerViewScrollListener.isLoading = false
            binding.pullToRefreshLayout.isRefreshing = true
            refreshList()
        }


        initRecyclerView()
    }

    private fun configureViewModel() {

    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.apply {
            adapter = CharityAdapter(this@ReviewCharityActivity)
            setHasFixedSize(true)
            setRecyclerViewPagination(this.layoutManager as LinearLayoutManager)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        swipeController = SwipeController(this, object : SwipeControllerActions() {
            override fun onRightClicked(position: Int) {
                viewModel.acceptCharity(viewModel.reviewItem[position].userId)
                        .observe(this@ReviewCharityActivity) { result ->
                            if (result.status == CustomResult.Status.SUCCESS) {
                                removeReviewedItem(position)
                            } else if (result.status == CustomResult.Status.ERROR) {
                                showToastMessage(getString(R.string.please_try_again))
                            }
                        }
            }

            override fun onLeftClicked(position: Int) {
                showGetInputDialog(Bundle().apply {
                    putString("title", getString(R.string.Please_write_reason_for_charity))
                    putString("hint", getString(R.string.reason_of_reject_for_charity))
                    putString("accept_btn", getString(R.string.reject_charity))
                }, approveListener = {
                    viewModel.rejectCharity(viewModel.reviewItem[position].userId, it)
                            .observe(this@ReviewCharityActivity) { result ->
                                if (result.status == CustomResult.Status.SUCCESS) {
                                    removeReviewedItem(position)
                                } else if (result.status == CustomResult.Status.ERROR) {
                                    showToastMessage(getString(R.string.please_try_again))
                                }
                            }
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

    private fun onCatalogItemsReceived(it: CustomResult<List<CharityModel>>) {
        when (it.status) {
            CustomResult.Status.LOADING -> {
//                showProgressDialog()
            }

            CustomResult.Status.SUCCESS -> {
                endlessRecyclerViewScrollListener.isLoading = false
                binding.pullToRefreshLayout.isRefreshing = false
                dismissProgressDialog()

                if (it.data != null && it.data.size < 20) {
                    endlessRecyclerViewScrollListener.isLoading = true
                } else if (it.data == null) {
                    endlessRecyclerViewScrollListener.isLoading = true
                }

                it.data?.let { data ->
                    viewModel.reviewItem.addAll(data)
                    showList()
                    checkEmptyState()
                }
            }

            CustomResult.Status.ERROR -> {
                endlessRecyclerViewScrollListener.isLoading = false
                dismissProgressDialog()
                showToastMessage(getString(R.string.please_try_again))
            }
        }
    }

    private fun checkEmptyState() {
        if (viewModel.reviewItem.isEmpty()) {
            binding.emptyStateTextView.visibility = View.VISIBLE
            binding.pullToRefreshLayout.visibility = View.GONE
        } else {
            binding.emptyStateTextView.visibility = View.GONE
            binding.pullToRefreshLayout.visibility = View.VISIBLE
        }
    }

    private fun showList() {
        (binding.itemsListRecyclerView.adapter as CharityAdapter).submitList(viewModel.reviewItem)
        (binding.itemsListRecyclerView.adapter as CharityAdapter).notifyDataSetChanged()
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        viewModel.clickedItemPosition = position
        CharityDetailActivity.start(this, obj as CharityModel)
    }

    private fun removeReviewedItem(position: Int) {
        viewModel.reviewItem.removeAt(position)
        (binding.itemsListRecyclerView.adapter as CharityAdapter)
                .notifyItemRemoved(position)
        checkEmptyState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GiftDetailActivity.GIFT_REVIEW_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val isReviews = data?.getBooleanExtra("isReviews", false) ?: false

                if (isReviews) {
                    removeReviewedItem(viewModel.clickedItemPosition)
                    viewModel.clickedItemPosition = -1
                }
            }
        }
    }
}
