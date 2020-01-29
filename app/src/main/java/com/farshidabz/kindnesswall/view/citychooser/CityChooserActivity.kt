package com.farshidabz.kindnesswall.view.citychooser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.databinding.ActivityCityChooserBinding
import org.koin.android.viewmodel.ext.android.viewModel

class CityChooserActivity : BaseActivity() {

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
    }

    override fun configureViews(savedInstanceState: Bundle?) {
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.chooseCityContainer).navigateUp()
}
