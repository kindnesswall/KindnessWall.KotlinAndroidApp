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
import com.farshidabz.kindnesswall.databinding.FragmentChooseCityBinding
import com.farshidabz.kindnesswall.view.citychooser.adapters.CityListAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class ChooseCityFragment : BaseFragment() {

    private val viewModel: CityChooserViewModel by sharedViewModel()

    lateinit var binding: FragmentChooseCityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_choose_city, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getCities()
    }

    override fun configureViews() {
        binding.backImageView.setOnClickListener { activity?.onBackPressed() }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.cityRecyclerView.adapter = CityListAdapter().apply {
        }

        binding.cityRecyclerView.setHasFixedSize(true)
        binding.cityRecyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
    }

    private fun getCities() {
        viewModel.cityList.observe(viewLifecycleOwner) {
            when (it.status) {
                CustomResult.Status.LOADING -> {
                }
                CustomResult.Status.SUCCESS -> {
                    it.data?.let { cities ->
                        (binding.cityRecyclerView.adapter as CityListAdapter).setItems(
                            cities
                        )
                    }
                }
                CustomResult.Status.ERROR -> {
                }
            }
        }
    }
}
