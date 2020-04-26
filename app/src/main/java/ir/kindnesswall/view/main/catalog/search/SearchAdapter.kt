package ir.kindnesswall.view.main.catalog.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.databinding.ItemCatalogBinding
import ir.kindnesswall.utils.OnItemClickListener

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
        )
    }

    private fun createOnClickListener(position: Int, item: GiftModel) =
        View.OnClickListener {
            clickListener.onItemClicked(position, item)
        }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.binding.item = items[position]

        holder.binding.clickListener =
            createOnClickListener(holder.adapterPosition, items[position])
    }
}

class SearchViewHolder(val binding: ItemCatalogBinding) : RecyclerView.ViewHolder(binding.root)