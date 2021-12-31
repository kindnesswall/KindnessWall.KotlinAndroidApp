package ir.kindnesswall.view.citychooser


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import ir.kindnesswall.BaseFragment
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CustomResult
import ir.kindnesswall.databinding.FragmentChooseCityBinding
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import ir.kindnesswall.view.citychooser.adapters.CityListAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCities()
    }

    override fun configureViews() {
        binding.backImageView.setOnClickListener { activity?.onBackPressed() }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.cityRecyclerView.adapter = CityListAdapter().apply {
            onClickCallback = { model ->
                viewModel.onClickCallback.onItemClicked(0, model)
            }
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
        viewModel.getCities().observe(viewLifecycleOwner) {
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
                    if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                        NoInternetDialogFragment().display(childFragmentManager) {
                            getCities()
                        }
                    } else {
                        showToastMessage(getString(R.string.please_try_again))
                    }
                }
            }
        }
    }
}
