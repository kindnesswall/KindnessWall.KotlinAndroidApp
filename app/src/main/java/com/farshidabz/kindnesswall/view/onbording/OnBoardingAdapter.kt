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

class OnBoardingAdapter(private var items: ArrayList<OnBoardingModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var onItemClickListener: (Int, OnBoardingModel) -> Unit
    private lateinit var onActionButtonClickListener :(OnBoardingModel) -> Unit

    fun setItems(items: ArrayList<OnBoardingModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 4) {
            return OnBoardingLayoutType.CITY
        }

        return OnBoardingLayoutType.BENEFITS
    }

    fun setOnItemClickListener(onItemClickListener: (Int, OnBoardingModel) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnActionButtonClickListener(onItemClickListener: (OnBoardingModel) -> Unit) {
        this.onActionButtonClickListener = onItemClickListener
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
            is OnBoardingCityViewHolder -> bindViewsToCityHolder(holder, item)
        }
    }

    private fun bindViewsToCityHolder(holder: OnBoardingCityViewHolder, item: OnBoardingModel) {
        holder.binding.chooseCityContainer.setOnClickListener {
            onItemClickListener.invoke(holder.adapterPosition, item)
        }

        holder.binding.actionButton.setOnClickListener {
            onActionButtonClickListener.invoke(item)
        }

        item.city?.let {
            if(it.id > 0){
                holder.binding.cityNameTextView.text = it.name
            }
        }
    }
}

class OnBoardingCityViewHolder(var binding: ItemOnboardingCityBinding) :
    RecyclerView.ViewHolder(binding.root)

class OnBoardingBenefitsViewHolder(var binding: ItemOnboardingBinding) :
    RecyclerView.ViewHolder(binding.root)