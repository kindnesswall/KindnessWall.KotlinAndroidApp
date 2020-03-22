package com.farshidabz.kindnesswall.view.citychooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.farshidabz.kindnesswall.BaseFragment
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.databinding.FragmentChooseRegionBinding
import com.farshidabz.kindnesswall.view.citychooser.adapters.RegionListAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ChooseRegionFragment : BaseFragment() {

    private val viewModel: CityChooserViewModel by sharedViewModel()

    lateinit var binding: FragmentChooseRegionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_region, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getRegions()
    }

    override fun configureViews() {
        binding.backImageView.setOnClickListener { activity?.onBackPressed() }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.regionRecyclerView.adapter = RegionListAdapter().apply {
            onClickCallback = { model ->
                viewModel.onClickCallback.onItemClicked(0, model)
            }
        }

        binding.regionRecyclerView.setHasFixedSize(true)
        binding.regionRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getRegions() {
        viewModel.getRegions().observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    it.data?.let { regions ->
                        (binding.regionRecyclerView.adapter as RegionListAdapter).setItems(
                            regions
                        )
                    }
                }

                CustomResult.Status.ERROR -> {
                }
            }
        }
    }
}