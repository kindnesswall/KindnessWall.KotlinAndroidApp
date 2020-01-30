package com.farshidabz.kindnesswall.view.citychooser


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import com.farshidabz.kindnesswall.BaseFragment
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.CustomResult
import com.farshidabz.kindnesswall.databinding.FragmentChooseProvinceBinding
import com.farshidabz.kindnesswall.view.citychooser.adapters.ProvinceListAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class ChooseProvinceFragment : BaseFragment() {
    private val viewModel: CityChooserViewModel by sharedViewModel()

    lateinit var binding: FragmentChooseProvinceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_choose_province, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        getProvinces()
    }

    override fun configureViews() {
        binding.backImageView.setOnClickListener { activity?.onBackPressed() }
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.provinceRecyclerView.adapter = ProvinceListAdapter().apply {
            onClickCallback = { model ->
                viewModel.onClickCallback.onItemClicked(0, model)
            }
        }

        binding.provinceRecyclerView.setHasFixedSize(true)
        binding.provinceRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getProvinces() {
        viewModel.provinceList.observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    it.data?.let { provinces ->
                        (binding.provinceRecyclerView.adapter as ProvinceListAdapter).setItems(
                            provinces
                        )
                    }
                }
                CustomResult.Status.ERROR -> {
                }
            }
        }
    }
}
