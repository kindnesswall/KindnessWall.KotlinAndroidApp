package ir.kindnesswall.view.category

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.SimpleItemAnimator
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CategoryModel
import ir.kindnesswall.databinding.ActivityCategoryBinding
import ir.kindnesswall.domain.common.CustomResult
import ir.kindnesswall.utils.OnItemClickListener
import ir.kindnesswall.utils.widgets.NoInternetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class CategoryActivity : BaseActivity() {

    lateinit var binding: ActivityCategoryBinding

    private val viewModel: CategoryViewModel by viewModel()

    companion object {
        const val CATEGORY_CHOOSER_REQUEST_CODE = 105

        @JvmStatic
        fun startActivityForResult(
            fragment: Fragment,
            multiSelection: Boolean = true,
            prvSelectedCategories: ArrayList<CategoryModel>? = null
        ) {
            val intent = Intent(fragment.activity, CategoryActivity::class.java)
            intent.putExtra("prvSelectedCategories", prvSelectedCategories)
            intent.putExtra("multiSelection", multiSelection)
            fragment.startActivityForResult(intent, CATEGORY_CHOOSER_REQUEST_CODE)
            fragment.activity?.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }

        @JvmStatic
        fun startActivityForResult(
            activity: AppCompatActivity,
            multiSelection: Boolean = true,
            prvSelectedCategories: ArrayList<CategoryModel>? = null
        ) {
            val intent = Intent(activity, CategoryActivity::class.java)
            intent.putExtra("prvSelectedCategories", prvSelectedCategories)
            intent.putExtra("multiSelection", multiSelection)
            activity.startActivityForResult(intent, CATEGORY_CHOOSER_REQUEST_CODE)
            activity.overridePendingTransition(R.anim.slide_up_activity, R.anim.stay)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category)

        viewModel.prvSelectedCategories =
            intent?.getSerializableExtra("prvSelectedCategories") as ArrayList<CategoryModel>?

        viewModel.multiSelection =
            intent?.getBooleanExtra("multiSelection", true) ?: true

        configureViews(savedInstanceState)
        getCategories()
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        binding.backImageView.setOnClickListener { returnResult(false) }
        binding.doneImageView.setOnClickListener { returnResult(true) }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val adapter = CategoryAdapter()
        adapter.setHasStableIds(true)

        binding.categoryRecyclerView.adapter = adapter.apply {
            onClickCallback = object : OnItemClickListener {
                override fun onItemClicked(position: Int, obj: Any?) {
                    if (!viewModel.multiSelection) {
                        for (item in viewModel.catgories) {
                            item.isSelected = false
                        }

                        viewModel.catgories[position].isSelected = true

                        binding.doneImageView.performClick()
                    } else {
                        viewModel.catgories[position].isSelected =
                            !viewModel.catgories[position].isSelected
                    }

                    (binding.categoryRecyclerView.adapter as CategoryAdapter).notifyDataSetChanged()
                }
            }
        }

        binding.categoryRecyclerView.setHasFixedSize(true)
        binding.categoryRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        val animator = binding.categoryRecyclerView.itemAnimator

        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    private fun getCategories() {
        viewModel.getCategories().observe(this) {
            when (it.status) {
                CustomResult.Status.LOADING -> showProgressDialog(
                    cancelable = true,
                    canceledOnTouchOutside = true
                )

                CustomResult.Status.ERROR -> {
                    dismissProgressDialog()

                    if (it.errorMessage?.message!!.contains("Unable to resolve host")) {
                        NoInternetDialogFragment().display(supportFragmentManager) {
                            getCategories()
                        }
                    } else {
                        showToastMessage(getString(R.string.please_try_again))
                    }
                }

                CustomResult.Status.SUCCESS -> {
                    dismissProgressDialog()
                    showList(it.data)
                }
            }
        }
    }

    private fun showList(data: List<CategoryModel>?) {
        data?.let {
            viewModel.prvSelectedCategories?.let { prvSelected ->
                if (prvSelected.size > 0) {
                    for (selectedCat in prvSelected) {
                        for (item in data) {
                            if (item.id == selectedCat.id) {
                                item.isSelected = true
                            }
                            continue
                        }
                    }
                }
            }

            viewModel.catgories.clear()

            viewModel.catgories.addAll(it as ArrayList<CategoryModel>)
            (binding.categoryRecyclerView.adapter as CategoryAdapter).setItems(
                viewModel.catgories,
                viewModel.multiSelection
            )
        }
    }

    private fun returnResult(result: Boolean) {
        if (result) {
            val returnIntent = Intent().apply {
                putExtra("selectedCategories", getSelectedCategories())
            }

            setResult(Activity.RESULT_OK, returnIntent)
            finish()

        } else {
            setResult(Activity.RESULT_CANCELED, null)
            finish()
        }
    }

    private fun getSelectedCategories(): ArrayList<CategoryModel> {
        val selectedCategories = arrayListOf<CategoryModel>()

        for (item in viewModel.catgories) {
            if (item.isSelected) {
                selectedCategories.add(item)
            }
        }

        return selectedCategories
    }
}
