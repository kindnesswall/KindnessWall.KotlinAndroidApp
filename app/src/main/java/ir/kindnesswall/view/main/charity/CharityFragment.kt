package ir.kindnesswall.view.main.charity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.FragmentCharityBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.helper.EndlessRecyclerViewScrollListener
import ir.kindnesswall.view.main.charity.charitydetail.CharityDetailActivity
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

class CharityFragment : BaseFragment(), OnItemClickListener {
    lateinit var binding: FragmentCharityBinding

    private val viewModel: CharityListViewModel by viewModel()

    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_charity, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getCharityFirstPage()
    }

    override fun configureViews() {
        binding.addImageView.setOnClickListener {
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.apply {
            adapter = CharityAdapter(this@CharityFragment)
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
        viewModel.getCharityItemsFromServer().observe(viewLifecycleOwner) {
            onItemsReceived(it)
        }
    }

    private fun getCharityFirstPage() {
        viewModel.charityItems.observe(viewLifecycleOwner) {
            onItemsReceived(it)
        }
    }

    private fun onItemsReceived(it: CustomResult<List<CharityModel>>) {
        when (it.status) {
            CustomResult.Status.LOADING -> {
                showProgressDialog()
            }

            CustomResult.Status.SUCCESS -> {
                showList(it.data)
            }

            CustomResult.Status.ERROR -> {
                Log.e(">>>>>>", it.message.toString())
                showToastMessage("")
            }
        }
    }

    private fun showList(data: List<CharityModel>?) {
        if (!data.isNullOrEmpty()) {
            (binding.itemsListRecyclerView.adapter as CharityAdapter).submitList(data)
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        context?.let { CharityDetailActivity.start(it, obj as CharityModel) }
    }
}