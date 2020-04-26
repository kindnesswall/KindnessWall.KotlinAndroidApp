package ir.kindnesswall.view.main.catalog.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.AppPref
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.data.model.FilterModel
import ir.kindnesswall.databinding.FragmentSearchCatalogBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.extentions.onDone
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import ir.kindnesswall.view.filter.FilterActivity
import ir.kindnesswall.view.giftdetail.GiftDetailActivity
import org.koin.android.viewmodel.ext.android.viewModel


/**
 * Created by farshid.abazari since 2019-11-07
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */
class SearchFragment : BaseFragment() {
    lateinit var binding: FragmentSearchCatalogBinding
    private val viewModel: SearchViewModel by viewModel()

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    private var searchEditTextHandler: Handler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_catalog, container, false)
        return binding.root
    }

    override fun configureViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        initGiftsRecyclerView()
        initPrvSearchRecyclerView()

        showPrvSearchList()

        binding.searchEditText.onDone { searchForWorld() }

        binding.searchEditText.doAfterTextChanged {
            searchEditTextHandler?.removeCallbacksAndMessages(null)
            searchEditTextHandler = null
            searchEditTextHandler = Handler()
            searchEditTextHandler?.postDelayed({ searchForWorld() }, 600)
        }

        binding.searchEditText.setOnClickListener { showPrvSearchList() }
        binding.searchImageView.setOnClickListener { /*todo*/ }
        binding.filterImageView.setOnClickListener { showFilterPage() }
    }

    private fun searchForWorld() {
        if (!viewModel.searchWorld.isNullOrEmpty()) {
            AppPref.addRecentSearch(viewModel.searchWorld!!)
        }

        viewModel.searchItems.value?.clear()
        binding.itemsListRecyclerView.adapter?.notifyDataSetChanged()

        viewModel.searchFirstPage().observe(viewLifecycleOwner) {
            onCatalogItemsReceived(it)
        }
    }

    private fun searchByFilter() {
        viewModel.searchItems.value?.clear()
        binding.itemsListRecyclerView.adapter?.notifyDataSetChanged()

        if (viewModel.filterModel == null) {
            showPrvSearchList()

            viewModel.searchItems.value?.clear()
            binding.itemsListRecyclerView.adapter?.notifyDataSetChanged()

            return
        }

        viewModel.searchFirstPage().observe(viewLifecycleOwner) {
            onCatalogItemsReceived(it)
        }
    }

    private fun initPrvSearchRecyclerView() {
        binding.prvSearchListRecyclerView.apply {
            adapter = PrvSearchAdapter(object : OnItemClickListener {
                override fun onItemClicked(position: Int, obj: Any?) {
                    binding.searchEditText.setText((obj as String))
                }
            })
            setHasFixedSize(true)
        }
        binding.prvSearchListRecyclerView.measuredHeight
    }

    private fun initGiftsRecyclerView() {
        val adapter = SearchAdapter(object : OnItemClickListener {
            override fun onItemClicked(position: Int, obj: Any?) {
                context?.let { GiftDetailActivity.start(it, obj as GiftModel) }
            }
        })

        adapter.setHasStableIds(true)

        binding.itemsListRecyclerView.apply {
            this.adapter = adapter
            setHasFixedSize(true)
            setRecyclerViewPagination(this.layoutManager as LinearLayoutManager)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

            val animator = itemAnimator

            if (animator is SimpleItemAnimator) {
                animator.supportsChangeAnimations = false
            }
        }
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

    private fun loadNextPage() {
        viewModel.searchForItemFromServer().observe(viewLifecycleOwner) {
            onCatalogItemsReceived(it)
        }
    }

    private fun onCatalogItemsReceived(it: CustomResult<List<GiftModel>>) {
        when (it.status) {
            CustomResult.Status.LOADING -> {
                showProgressDialog()
            }

            CustomResult.Status.SUCCESS -> {
                hidePrvSearchList()

                if (it.data != null) {
                    if (viewModel.searchItems.value == null) {
                        viewModel.searchItems.value = it.data as ArrayList<GiftModel>?
                    } else {
                        viewModel.searchItems.value?.addAll(it.data)
                    }

                    showList(viewModel.searchItems.value)
                }
            }

            CustomResult.Status.ERROR -> {
                showToastMessage("")
            }
        }
    }

    private fun showList(data: List<GiftModel>?) {
        if (!data.isNullOrEmpty()) {
            binding.itemsListRecyclerView.visibility = View.VISIBLE
            (binding.itemsListRecyclerView.adapter as SearchAdapter).setItems(data as ArrayList<GiftModel>)
        }
    }

    private fun showPrvSearchList() {
        val prvSearch = viewModel.getPrvSearchItems()
        if (prvSearch.isNullOrEmpty()) {
            hidePrvSearchList()
            return
        }

        binding.prvSearchContainer.visibility = View.VISIBLE
        (binding.prvSearchListRecyclerView.adapter as PrvSearchAdapter).submitList(prvSearch)
    }

    private fun hidePrvSearchList() {
        binding.prvSearchContainer.visibility = View.GONE
    }

    private fun showFilterPage() {
        FilterActivity.startActivityForResult(this, viewModel.filterModel)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FilterActivity.FILTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.filterModel =
                    data?.getSerializableExtra("filterModel") as FilterModel
                searchByFilter()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        searchEditTextHandler?.removeCallbacksAndMessages(null)
        searchEditTextHandler = null
    }

    override fun onDestroy() {
        super.onDestroy()

        searchEditTextHandler?.removeCallbacksAndMessages(null)
        searchEditTextHandler = null
    }

    override fun onStop() {
        super.onStop()

        searchEditTextHandler?.removeCallbacksAndMessages(null)
        searchEditTextHandler = null
    }
}