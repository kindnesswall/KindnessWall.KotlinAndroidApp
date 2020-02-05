package com.farshidabz.kindnesswall.view.charity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.data.local.dao.charity.CharityModel
import com.farshidabz.kindnesswall.databinding.ItemCharityBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener

class CharityAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<CharityModel, CatalogViewHolder>(CatalogDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CatalogViewHolder(
            ItemCharityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) =
        with(getItem(position)) {
            holder.bind(
                this,
                createOnClickListener(position, getItem(position))
            )
        }

    private fun createOnClickListener(position: Int, item: CharityModel) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }
}

class CatalogViewHolder(val binding: ItemCharityBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: CharityModel, listener: View.OnClickListener) =
        with(binding) {
            this.item = item
            clickListener = listener
            executePendingBindings()
        }
}


private class CatalogDiffUtil : DiffUtil.ItemCallback<CharityModel>() {
    override fun areItemsTheSame(oldItem: CharityModel, newItem: CharityModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CharityModel, newItem: CharityModel): Boolean =
        oldItem == newItem
}