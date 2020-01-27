package com.farshidabz.kindnesswall.view.onbording

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.BaseActivity
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.data.model.OnBoardingModel
import com.farshidabz.kindnesswall.databinding.ActivityOnBoardingBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener
import com.farshidabz.kindnesswall.utils.extentions.getSnapPosition
import com.farshidabz.kindnesswall.view.main.MainActivity
import java.util.*

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding)

        configureViews(savedInstanceState)
    }

    override fun configureViews(savedInstanceState: Bundle?) {
        initRecyclerView()

        binding.nextTextView.setOnClickListener {
            if (snapPosition == 3) {
                MainActivity.start(this)
                finish()
                return@setOnClickListener
            }

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

        (binding.onBoardingRecyclerView.adapter as OnBoardingAdapter).setOnItemClickListener(object :
            OnItemClickListener {
            override fun onItemClicked(position: Int, obj: Any?) {

            }
        })

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
        }
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

        if (requestCode == 125) {
            if (resultCode == Activity.RESULT_OK) {
                val items = getOnBoardingModel()
                items[4].city = data?.getSerializableExtra("city") as CityModel
                (binding.onBoardingRecyclerView.adapter as OnBoardingAdapter).setItems(items)
            }
        }
    }
}
