package ir.kindnesswall.view.main.catalog.cataloglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.FragmentCatalogBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
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

class CatalogFragment : BaseFragment(), OnItemClickListener {
    private val viewModel: CatalogViewModel by viewModel()

    lateinit var binding: FragmentCatalogBinding

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_catalog, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.catalogItems.isNullOrEmpty()) {
            getGifts()
        } else {
            showList()
        }
    }

    override fun configureViews() {
        binding.searchImageView.setOnClickListener {
            it.findNavController()
                .navigate(CatalogFragmentDirections.actionCatalogFragmentToSearchFragment())
        }

        binding.pullToRefreshLayout.setOnRefreshListener {
            endlessRecyclerViewScrollListener.isLoading = false
            binding.pullToRefreshLayout.isRefreshing = true
            refreshList()
        }

        initRecyclerView()
    }

    private fun refreshList() {
        viewModel.catalogItems.clear()
        showList()
        getGifts()
    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.apply {
            adapter = CatalogAdapter(this@CatalogFragment)
            setHasFixedSize(true)
            setRecyclerViewPagination(this.layoutManager as LinearLayoutManager)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setRecyclerViewPagination(layoutManager: LinearLayoutManager) {
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore() {
                    endlessRecyclerViewScrollListener.isLoading = true
                    getGifts()
                }

                override fun onScrolled(position: Int) {
                }
            }

        binding.itemsListRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun getGifts() {
        viewModel.getCatalogItemsFromServer().observe(viewLifecycleOwner) {
            onCatalogItemsReceived(it)
        }
    }

    private fun onCatalogItemsReceived(it: CustomResult<List<GiftModel>>) {
        when (it.status) {
            CustomResult.Status.LOADING -> {
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
                    viewModel.catalogItems.addAll(data)
                    showList()
                }
            }

            CustomResult.Status.ERROR -> {
                binding.pullToRefreshLayout.isRefreshing = false
                endlessRecyclerViewScrollListener.isLoading = false
                showToastMessage("")
            }
        }
    }

    private fun showList() {
        (binding.itemsListRecyclerView.adapter as CatalogAdapter).submitList(viewModel.catalogItems)
        (binding.itemsListRecyclerView.adapter as CatalogAdapter).notifyDataSetChanged()
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        context?.let { GiftDetailActivity.start(it, obj as GiftModel) }
    }
}