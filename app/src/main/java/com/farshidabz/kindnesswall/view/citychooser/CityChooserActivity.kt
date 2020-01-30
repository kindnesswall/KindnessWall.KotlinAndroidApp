package com.farshidabz.kindnesswall.view.citychooser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.dao.province.ProvinceModel
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.databinding.ActivityCityChooserBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener
import org.koin.android.viewmodel.ext.android.viewModel


class CityChooserActivity : BaseActivity(), OnItemClickListener {
    lateinit var binding: ActivityCityChooserBinding

    private val viewModel: CityChooserViewModel by viewModel()

    companion object {
        const val CITY_CHOOSER_REQUEST_CODE = 125

        @JvmStatic
        fun startActivityForResult(fragment: Fragment) {
            val intent = Intent(fragment.activity, CityChooserActivity::class.java)
            fragment.startActivityForResult(intent, CITY_CHOOSER_REQUEST_CODE)
            fragment.activity?.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }

        @JvmStatic
        fun startActivityForResult(activity: AppCompatActivity) {
            val intent = Intent(activity, CityChooserActivity::class.java)
            activity.startActivityForResult(intent, CITY_CHOOSER_REQUEST_CODE)
            activity.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_chooser)

        configureViews(savedInstanceState)
        configureViewModel()
    }

    override fun configureViews(savedInstanceState: Bundle?) {
    }

    private fun configureViewModel() {
        viewModel.onClickCallback = this
    }

    override fun onBackPressed() {
        when (findNavController(R.id.chooseCityContainer).currentDestination?.id) {
            R.id.chooseProvinceFragment -> {
                returnResult(false)
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun gotoCityFragment() {
        val action =
            ChooseProvinceFragmentDirections.actionChooseProvinceFragmentToChooseCityFragment(
                viewModel.chosenProvince.id
            )

        findNavController(R.id.chooseCityContainer).navigate(action)
    }

    private fun returnResult(result: Boolean) {
        if (result) {
            val returnIntent = Intent().apply {
                putExtra("city", viewModel.chosenCity)
            }

            setResult(Activity.RESULT_OK, returnIntent)
            finish()

        } else {
            setResult(Activity.RESULT_CANCELED, null)
            finish()
        }
    }

    override fun onItemClicked(position: Int, obj: Any?) {
        when (obj) {
            is ProvinceModel -> {
                viewModel.chosenProvince = obj
                gotoCityFragment()
            }

            is CityModel -> {
                viewModel.chosenCity = obj
                returnResult(true)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.chooseCityContainer).navigateUp()
}
