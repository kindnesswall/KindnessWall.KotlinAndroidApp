package com.farshidabz.kindnesswall.view.catalog.cataloglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.data.model.BaseModel
import com.farshidabz.kindnesswall.databinding.FragmentCatalogBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener

class CatalogAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<BaseModel, CatalogViewHolder>(CatalogDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CatalogViewHolder(
            FragmentCatalogBinding.inflate(
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

    private fun createOnClickListener(position: Int, item: BaseModel) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }

}

class CatalogViewHolder(private val binding: FragmentCatalogBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BaseModel, listener: View.OnClickListener) =
        with(binding) {
            //            this.item = item
//            clickListener = listener
//            executePendingBindings()
        }
}


private class CatalogDiffUtil : DiffUtil.ItemCallback<BaseModel>() {
    override fun areItemsTheSame(oldItem: BaseModel, newItem: BaseModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: BaseModel, newItem: BaseModel): Boolean =
        oldItem.id == newItem.id
}