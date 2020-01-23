package com.farshidabz.kindnesswall.view.catalog.cataloglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.databinding.ItemCatalogBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener

class CatalogAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<GiftModel, CatalogViewHolder>(CatalogDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CatalogViewHolder(
            ItemCatalogBinding.inflate(
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

    private fun createOnClickListener(position: Int, item: GiftModel) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }

}

class CatalogViewHolder(val binding: ItemCatalogBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GiftModel, listener: View.OnClickListener) =
        with(binding) {
            this.item = item
            clickListener = listener
            executePendingBindings()
        }
}


private class CatalogDiffUtil : DiffUtil.ItemCallback<GiftModel>() {
    override fun areItemsTheSame(oldItem: GiftModel, newItem: GiftModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GiftModel, newItem: GiftModel): Boolean = false
}