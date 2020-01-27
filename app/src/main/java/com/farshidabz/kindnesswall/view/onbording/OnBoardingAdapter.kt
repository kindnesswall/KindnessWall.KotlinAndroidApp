package com.farshidabz.kindnesswall.view.onbording

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.annotation.OnBoardingLayoutType
import com.farshidabz.kindnesswall.data.model.OnBoardingModel
import com.farshidabz.kindnesswall.databinding.ItemOnboardingBinding
import com.farshidabz.kindnesswall.databinding.ItemOnboardingCityBinding

class OnBoardingAdapter(var items: ArrayList<OnBoardingModel> = arrayListOf()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        if (position == 4) {
            return OnBoardingLayoutType.CITY
        }

        return OnBoardingLayoutType.BENEFITS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            OnBoardingLayoutType.BENEFITS -> OnBoardingBenefitsViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_onboarding,
                    parent,
                    false
                )
            )

            OnBoardingLayoutType.CITY -> OnBoardingCityViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_onboarding_city,
                    parent,
                    false
                )
            )

            else -> OnBoardingBenefitsViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_onboarding,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is OnBoardingBenefitsViewHolder -> holder.binding.item = item
            is OnBoardingCityViewHolder -> bindViewsToCityHolder(holder)
        }
    }

    private fun bindViewsToCityHolder(holder: OnBoardingCityViewHolder) {
        holder.binding.chooseCityContainer.setOnClickListener {

        }
    }
}

class OnBoardingCityViewHolder(var binding: ItemOnboardingCityBinding) :
    RecyclerView.ViewHolder(binding.root)

class OnBoardingBenefitsViewHolder(var binding: ItemOnboardingBinding) :
    RecyclerView.ViewHolder(binding.root)