package ir.kindnesswall.view.citychooser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.model.RegionModel
import ir.kindnesswall.databinding.ItemRegionListBinding

class RegionListAdapter : RecyclerView.Adapter<RegionListViewHolder>() {

    private var items = ArrayList<RegionModel>()

    fun setItems(items: List<RegionModel>) {
        this.items = items as ArrayList<RegionModel>
        notifyDataSetChanged()
    }

    public lateinit var onClickCallback: (RegionModel) -> Unit?

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegionListViewHolder {
        return RegionListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_region_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RegionListViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.itemView.setOnClickListener { onClickCallback.invoke(items[holder.adapterPosition]) }
    }
}

class RegionListViewHolder(val binding: ItemRegionListBinding) :
    RecyclerView.ViewHolder(binding.root)