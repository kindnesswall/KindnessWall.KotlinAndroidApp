package com.farshidabz.kindnesswall.view.catalog.cataloglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.farshidabz.kindnesswall.BaseFragment
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.data.model.gift.GiftResponseModel
import com.farshidabz.kindnesswall.databinding.FragmentCatalogBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener
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

    private fun getGiftsFirstPage() {
        viewModel.catalogItems.observe(viewLifecycleOwner) {
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
    }

    private fun showList(data: GiftResponseModel?) {
        data?.giftResponseModel?.let {
            (binding.itemsListRecyclerView.adapter as CatalogAdapter).submitList(it)
        }
    }

    override fun configureViewModel() {
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
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {

    }
}