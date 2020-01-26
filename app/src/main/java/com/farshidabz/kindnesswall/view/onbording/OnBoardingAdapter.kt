package com.farshidabz.kindnesswall.view.onbording

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.OnBoardingModel
import com.farshidabz.kindnesswall.databinding.ItemOnboardingBinding

class OnBoardingAdapter(var items: ArrayList<OnBoardingModel> = arrayListOf()) :
    RecyclerView.Adapter<OnBoardingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        return OnBoardingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_onboarding,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: OnBoardingViewHolder, position: Int) {
        holder.binding.item = items[position]
    }
}

class OnBoardingViewHolder(var binding: ItemOnboardingBinding) :
    RecyclerView.ViewHolder(binding.root)