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
import com.farshidabz.kindnesswall.databinding.ActivityFilterBinding
import com.farshidabz.kindnesswall.utils.extentions.dp
import com.farshidabz.kindnesswall.view.category.CategoryActivity
import com.farshidabz.kindnesswall.view.citychooser.CityChooserActivity


class FilterActivity : BaseActivity() {
    lateinit var binding: ActivityFilterBinding

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

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { }
        binding.removeAllFilters.setOnClickListener { }
        binding.doneImageView.setOnClickListener { }

        binding.selectCategoryContainer.setOnClickListener {
            CategoryActivity.startActivityForResult(this)
        }

        binding.selectCityContainer.setOnClickListener {
            CityChooserActivity.startActivityForResult(this)
        }
    }

    private fun showSelectedCategories(categoryModels: ArrayList<CategoryModel>?) {
        binding.selectedCategoriesLinearLayout.removeAllViews()

        if (categoryModels == null) {
            return
        }

        if (categoryModels.size == 0) {
            return
        }

        for (categoryModel in categoryModels) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CityChooserActivity.CITY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val cityModel = data?.getSerializableExtra("city") as CityModel
            }
        } else if (requestCode == CategoryActivity.CATEGORY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val categoryModel =
                    data?.getSerializableExtra("selectedCategories") as ArrayList<CategoryModel>
                showSelectedCategories(categoryModel)
            }
        }
    }
}
