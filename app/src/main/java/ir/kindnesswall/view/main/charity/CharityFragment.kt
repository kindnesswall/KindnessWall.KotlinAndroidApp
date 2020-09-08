package ir.kindnesswall.view.main.charity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.charity.CharityModel
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.FragmentCharityBinding
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.main.charity.add.AddCharityFragment
import ir.kindnesswall.view.main.charity.charitydetail.CharityDetailActivity
import kotlinx.android.synthetic.main.toolbar_action_button_layout.*
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_charity, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModel.charityItems.isNullOrEmpty()) {
            getCharities()
        } else {
            showList()
        }
    }

    override fun configureViews() {
        actionButton.setOnClickListener {
            val addCharityFragment = AddCharityFragment()
            addCharityFragment.show(childFragmentManager, addCharityFragment.tag)
        }

        binding.pullToRefreshLayout.setOnRefreshListener {
            binding.pullToRefreshLayout.isRefreshing = true
            refreshList()
        }

        initRecyclerView()
    }

    private fun refreshList() {
        viewModel.charityItems.clear()
        showList()
        getCharities()
    }

    private fun getCharities() {
        viewModel.getCharityItemsFromServer().observe(viewLifecycleOwner) {
            onItemsReceived(it)
        }
    }

    private fun initRecyclerView() {
        binding.itemsListRecyclerView.apply {
            adapter = CharityAdapter(this@CharityFragment)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    private fun onItemsReceived(it: CustomResult<List<CharityModel>>) {
        when (it.status) {
            CustomResult.Status.LOADING -> {
                showProgressDialog()
            }

            CustomResult.Status.SUCCESS -> {
                binding.pullToRefreshLayout.isRefreshing = false
                it.data?.let { data ->
                    viewModel.charityItems.addAll(data)
                    showList()
                }
            }

            CustomResult.Status.ERROR -> {
                if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                    NoInternetDialogFragment().display(childFragmentManager) {
                        getCharities()
                    }
                } else {
                    showToastMessage(getString(R.string.please_try_again))
                }
            }
        }
    }

    private fun showList() {
        (binding.itemsListRecyclerView.adapter as CharityAdapter).submitList(viewModel.charityItems)
        (binding.itemsListRecyclerView.adapter as CharityAdapter).notifyDataSetChanged()
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        context?.let { CharityDetailActivity.start(it, obj as CharityModel) }
    }
}