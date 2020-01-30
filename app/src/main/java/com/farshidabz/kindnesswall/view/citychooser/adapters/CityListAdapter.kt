package com.farshidabz.kindnesswall.view.citychooser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.model.CityModel
import com.farshidabz.kindnesswall.databinding.ItemCityListBinding

class CityListAdapter : RecyclerView.Adapter<CityListViewHolder>() {

    private var items = ArrayList<CityModel>()

    fun setItems(items: List<CityModel>) {
        this.items = items as ArrayList<CityModel>
        notifyDataSetChanged()
    }

    public lateinit var onClickCallback: (CityModel) -> Unit?

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityListViewHolder {
        return CityListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_city_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CityListViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.itemView.setOnClickListener { onClickCallback.invoke(items[holder.adapterPosition]) }
    }
}

class CityListViewHolder(val binding: ItemCityListBinding) :
    RecyclerView.ViewHolder(binding.root)