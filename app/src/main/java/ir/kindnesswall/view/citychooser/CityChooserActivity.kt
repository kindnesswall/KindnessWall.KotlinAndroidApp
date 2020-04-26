package ir.kindnesswall.view.citychooser

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.province.ProvinceModel
import ir.kindnesswall.data.model.CityModel
import ir.kindnesswall.data.model.RegionModel
import ir.kindnesswall.databinding.ActivityCityChooserBinding
import ir.kindnesswall.utils.OnItemClickListener
import org.koin.android.viewmodel.ext.android.viewModel


class CityChooserActivity : BaseActivity(), OnItemClickListener {
    private var chooseRegion: Boolean = false

    lateinit var binding: ActivityCityChooserBinding

    private val viewModel: CityChooserViewModel by viewModel()

    companion object {
        const val CITY_CHOOSER_REQUEST_CODE = 125

        @JvmStatic
        fun startActivityForResult(fragment: Fragment, chooseRegion: Boolean = false) {
            val intent = Intent(fragment.activity, CityChooserActivity::class.java)
            intent.putExtra("chooseRegion", chooseRegion)
            fragment.startActivityForResult(intent, CITY_CHOOSER_REQUEST_CODE)
            fragment.activity?.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }

        @JvmStatic
        fun startActivityForResult(activity: AppCompatActivity, chooseRegion: Boolean = false) {
            val intent = Intent(activity, CityChooserActivity::class.java)
            intent.putExtra("chooseRegion", chooseRegion)
            activity.startActivityForResult(intent, CITY_CHOOSER_REQUEST_CODE)
            activity.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_city_chooser)

        chooseRegion = intent?.getBooleanExtra("chooseRegion", false) ?: false

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


    private fun gotoRegionFragment() {
        viewModel.chosenCity?.id?.let {
            val action =
                ChooseCityFragmentDirections.actionChooseCityFragmentToChooseRegionFragment(it)

            findNavController(R.id.chooseCityContainer).navigate(action)
        }
    }

    private fun returnResult(result: Boolean) {
        if (result) {
            val returnIntent = Intent()

            if (chooseRegion && viewModel.chosenCity?.hasRegions == true) {
                returnIntent.putExtra("region", viewModel.chosenRegions)
            } else {
                returnIntent.putExtra("city", viewModel.chosenCity)
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

                if (!chooseRegion || viewModel.chosenCity?.hasRegions == false) {
                    returnResult(true)
                } else {
                    gotoRegionFragment()
                }
            }

            is RegionModel -> {
                viewModel.chosenRegions = obj
                returnResult(true)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.chooseCityContainer).navigateUp()
}
