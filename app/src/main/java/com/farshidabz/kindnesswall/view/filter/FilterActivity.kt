package com.farshidabz.kindnesswall.view.filter

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.CategoryModel
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.data.model.FilterModel
import com.farshidabz.kindnesswall.data.model.RegionModel
import com.farshidabz.kindnesswall.databinding.ActivityFilterBinding
import com.farshidabz.kindnesswall.utils.extentions.dp
import com.farshidabz.kindnesswall.view.category.CategoryActivity
import com.farshidabz.kindnesswall.view.citychooser.CityChooserActivity


class FilterActivity : BaseActivity() {
    private var selectedRegion: RegionModel? = RegionModel()
    private var selectedCity: CityModel? = CityModel()

    private var categoryModels: ArrayList<CategoryModel>? = arrayListOf()

    lateinit var binding: ActivityFilterBinding

    private var prvFilterModel: FilterModel? = FilterModel()

    companion object {
        const val FILTER_REQUEST_CODE = 125

        @JvmStatic
        fun startActivityForResult(fragment: Fragment, filterModel: FilterModel? = null) {
            val intent = Intent(fragment.activity, FilterActivity::class.java)
            intent.putExtra("prvFilter", filterModel)
            fragment.startActivityForResult(intent, FILTER_REQUEST_CODE)
            fragment.activity?.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }

        @JvmStatic
        fun startActivityForResult(activity: AppCompatActivity, filterModel: FilterModel? = null) {
            val intent = Intent(activity, FilterActivity::class.java)
            intent.putExtra("prvFilter", filterModel)
            activity.startActivityForResult(intent, FILTER_REQUEST_CODE)
            activity.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filter)

        prvFilterModel = intent?.getSerializableExtra("prvFilter") as FilterModel?

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { returnResult(false) }
        binding.removeAllFilters.setOnClickListener { removeAllFilters() }
        binding.doneImageView.setOnClickListener { returnResult(true) }

        binding.selectCategoryContainer.setOnClickListener {
            CategoryActivity.startActivityForResult(this)
        }

        binding.selectCityContainer.setOnClickListener {
            CityChooserActivity.startActivityForResult(this, true)
        }

        prvFilterModel?.let {
            categoryModels = it.categoryModel
            selectedCity = it.city
            selectedRegion = it.regionModel

            showSelectedCategories()

            if (selectedRegion != null) {
                showSelectedRegions()
            } else if (selectedCity != null) {
                showSelectedCity()
            }
        }
    }

    private fun removeAllFilters() {
        categoryModels?.clear()

        categoryModels = null
        selectedRegion = null
        selectedCity = null

        showSelectedCategories()
        showSelectedCity()
        showSelectedRegions()
    }

    private fun showSelectedCategories() {
        binding.selectedCategoriesLinearLayout.removeAllViews()

        if (categoryModels == null) {
            return
        }

        if (categoryModels!!.size == 0) {
            return
        }

        for (categoryModel in categoryModels!!) {
            val selectedFilterLayout = layoutInflater.inflate(R.layout.item_selected_filter, null)
            val textView = selectedFilterLayout.findViewById<TextView>(R.id.selectedCategoryTitle)
            textView.text = categoryModel.title

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            val margin = 8.dp(this)
            params.setMargins(margin, margin, margin, margin)

            selectedFilterLayout.layoutParams = params

            selectedFilterLayout.minimumWidth = 70.dp(this)

            binding.selectedCategoriesLinearLayout.addView(selectedFilterLayout)
        }
    }

    private fun showSelectedRegions() {
        binding.selectedRegionsLinearLayout.removeAllViews()

        if (selectedRegion == null) {
            return
        }

        val selectedFilterLayout = layoutInflater.inflate(R.layout.item_selected_filter, null)
        val textView = selectedFilterLayout.findViewById<TextView>(R.id.selectedCategoryTitle)

        textView.text = selectedRegion!!.name.toString()

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val margin = 8.dp(this)
        params.setMargins(margin, margin, margin, margin)

        selectedFilterLayout.layoutParams = params

        selectedFilterLayout.minimumWidth = 70.dp(this)

        selectedFilterLayout.setOnClickListener { view ->
            binding.selectedRegionsLinearLayout.removeView(view)
            binding.selectedRegionsLinearLayout.requestLayout()
        }

        binding.selectedRegionsLinearLayout.addView(selectedFilterLayout)
    }

    private fun showSelectedCity() {
        binding.selectedRegionsLinearLayout.removeAllViews()

        if (selectedCity == null) {
            return
        }

        val selectedFilterLayout = layoutInflater.inflate(R.layout.item_selected_filter, null)
        val textView = selectedFilterLayout.findViewById<TextView>(R.id.selectedCategoryTitle)

        textView.text = selectedCity!!.name.toString()

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val margin = 8.dp(this)
        params.setMargins(margin, margin, margin, margin)

        selectedFilterLayout.layoutParams = params

        selectedFilterLayout.minimumWidth = 70.dp(this)

        selectedFilterLayout.setOnClickListener { view ->
            binding.selectedRegionsLinearLayout.removeView(view)
            binding.selectedRegionsLinearLayout.requestLayout()
        }

        binding.selectedRegionsLinearLayout.addView(selectedFilterLayout)
    }

    private fun returnResult(result: Boolean) {
        if (result) {
            val returnIntent = Intent()

            val filterModel: FilterModel?

            if (categoryModels == null && selectedRegion == null && selectedCity == null) {
                filterModel = null
            } else {
                filterModel = FilterModel().apply {
                    categoryModel = categoryModels
                    regionModel = selectedRegion
                    city = selectedCity
                }
            }


            returnIntent.putExtra("filterModel", filterModel)

            setResult(Activity.RESULT_OK, returnIntent)
            finish()

        } else {
            setResult(Activity.RESULT_CANCELED, null)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CityChooserActivity.CITY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val region = data?.getSerializableExtra("region")
                val cityModel = data?.getSerializableExtra("city")
                if (region != null) {
                    selectedRegion = region as RegionModel?
                    showSelectedRegions()
                } else if (cityModel != null) {
                    selectedCity = cityModel as CityModel?
                    showSelectedCity()
                }
            }
        } else if (requestCode == CategoryActivity.CATEGORY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                categoryModels =
                    data?.getSerializableExtra("selectedCategories") as ArrayList<CategoryModel>
                showSelectedCategories()
            }
        }
    }
}
