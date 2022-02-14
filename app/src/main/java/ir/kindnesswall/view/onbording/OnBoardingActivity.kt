package ir.kindnesswall.view.onbording

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.BaseActivity
import ir.kindnesswall.R
import ir.kindnesswall.data.model.CityModel
import ir.kindnesswall.data.model.OnBoardingModel
import ir.kindnesswall.databinding.ActivityOnBoardingBinding
import ir.kindnesswall.utils.extentions.getSnapPosition
import ir.kindnesswall.view.citychooser.CityChooserActivity
import ir.kindnesswall.view.main.MainActivity

class OnBoardingActivity : BaseActivity() {
    private var snapPosition: Int = 0

    lateinit var binding: ActivityOnBoardingBinding

    companion object {
        @JvmStatic
        fun start(context: Context) {
            context.startActivity(Intent(context, OnBoardingActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        initRecyclerView()

        binding.nextTextView.setOnClickListener {
            binding.onBoardingRecyclerView.smoothScrollToPosition(
                snapPosition + 1
            )
        }

        binding.skipTextView.setOnClickListener {
            MainActivity.start(this)
            finish()
        }
    }

    private fun initRecyclerView() {
        binding.onBoardingRecyclerView.adapter = OnBoardingAdapter(getOnBoardingModel())

        (binding.onBoardingRecyclerView.adapter as OnBoardingAdapter).setOnItemClickListener { _, _ ->
            CityChooserActivity.startActivityForResult(this)
        }

        (binding.onBoardingRecyclerView.adapter as OnBoardingAdapter).setOnActionButtonClickListener { model ->
            when {
                model.city == null -> showToastMessage(getString(R.string.choose_your_city))
                model.city!!.name.isNullOrEmpty() -> showToastMessage(getString(R.string.choose_your_city))
                else -> {
                    MainActivity.start(this@OnBoardingActivity)
                    this@OnBoardingActivity.finish()
                }
            }
        }

        binding.recyclerPagerIndicator.attachToRecyclerView(binding.onBoardingRecyclerView)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.onBoardingRecyclerView)

        binding.onBoardingRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                maybeNotifySnapPositionChange(recyclerView, snapHelper)
            }
        })
    }

    private fun maybeNotifySnapPositionChange(
        recyclerView: RecyclerView,
        snapHelper: PagerSnapHelper
    ) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            this.snapPosition = snapPosition

            if (snapPosition == 4) {
                hideBottomButtons()
            } else {
                showBottomButtons()
            }
        }
    }

    private fun hideBottomButtons() {
        binding.nextTextView.visibility = View.INVISIBLE
        binding.skipTextView.visibility = View.INVISIBLE
    }

    private fun showBottomButtons() {
        binding.nextTextView.visibility = View.VISIBLE
        binding.skipTextView.visibility = View.VISIBLE
    }

    private fun getOnBoardingModel(): ArrayList<OnBoardingModel> {
        val items = arrayListOf<OnBoardingModel>()

        items.add(OnBoardingModel().apply {
            title = getString(R.string.onboarding_title_first)
            descrption = getString(R.string.onboarding_desc_first)
            imageId = R.drawable.ic_gift
            backgroundColor = R.color.onBoardingFirstColor
        })

        items.add(OnBoardingModel().apply {
            title = getString(R.string.onboarding_title_second)
            descrption = getString(R.string.onboarding_desc_second)
            imageId = R.drawable.ic_charity_tour
            backgroundColor = R.color.onBoardingSecondColor
        })

        items.add(OnBoardingModel().apply {
            title = getString(R.string.onboarding_title_third)
            descrption = getString(R.string.onboarding_desc_third)
            imageId = R.drawable.ic_free
            backgroundColor = R.color.onBoardingThirdColor
        })

        items.add(OnBoardingModel().apply {
            title = getString(R.string.onboarding_title_fourth)
            descrption = getString(R.string.onboarding_desc_fourth)
            imageId = R.drawable.ic_opensource
            backgroundColor = R.color.onBoardingFourthColor
        })

        items.add(OnBoardingModel())

        return items
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CityChooserActivity.CITY_CHOOSER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val items = getOnBoardingModel()
                items[4].city = data?.getSerializableExtra("city") as CityModel
                (binding.onBoardingRecyclerView.adapter as OnBoardingAdapter).setItems(items)
            }
        }
    }
}
