package com.farshidabz.kindnesswall.view.catalog.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.data.local.dao.catalog.GiftModel
import com.farshidabz.kindnesswall.databinding.ItemCatalogBinding
import com.farshidabz.kindnesswall.utils.OnItemClickListener

class SearchAdapter(var clickListener: OnItemClickListener) :
    RecyclerView.Adapter<SearchViewHolder>() {
    private var items = ArrayList<GiftModel>()

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    fun setItems(searchResult: ArrayList<GiftModel>) {
        items = searchResult
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_catalog,
                parent,
                false
            )
        ).apply {
            binding.clickListener = clickListener
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.item = items[position]
    }
}

class SearchViewHolder(val binding: ItemCatalogBinding) : RecyclerView.ViewHolder(binding.root)