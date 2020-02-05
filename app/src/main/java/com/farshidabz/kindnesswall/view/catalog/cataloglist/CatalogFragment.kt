package com.farshidabz.kindnesswall.view.catalog.cataloglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.farshidabz.kindnesswall.BaseFragment
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.databinding.FragmentCatalogBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener
import com.farshidabz.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import com.farshidabz.kindnesswall.view.giftdetail.GiftDetailActivity
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getGiftsFirstPage()
    }

    override fun configureViews() {
        binding.searchImageView.setOnClickListener {
            it.findNavController()
                .navigate(CatalogFragmentDirections.actionCatalogFragmentToSearchFragment())
        }

        initRecyclerView()
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
                    loadNextPage()
                }

                override fun onScrolled(position: Int) {
                }
            }

        binding.itemsListRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun loadNextPage() {
        viewModel.getCatalogItemsFromServer().observe(viewLifecycleOwner) {
            onCatalogItemsReceived(it)
        }
    }

    private fun getGiftsFirstPage() {
        viewModel.catalogItems.observe(viewLifecycleOwner) {
            onCatalogItemsReceived(it)
        }
    }

    private fun onCatalogItemsReceived(it: CustomResult<List<GiftModel>>) {
        when (it.status) {
            CustomResult.Status.LOADING -> {
                showProgressDialog()
            }

            CustomResult.Status.SUCCESS -> {
                showList(it.data)
            }

            CustomResult.Status.ERROR -> {
                showToastMessage("")
            }
        }
    }

    private fun showList(data: List<GiftModel>?) {
        if (!data.isNullOrEmpty()) {
            (binding.itemsListRecyclerView.adapter as CatalogAdapter).submitList(data)
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        context?.let { GiftDetailActivity.start(it, obj as GiftModel) }
    }
}