package ir.kindnesswall.view.citychooser.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.province.ProvinceModel
import ir.kindnesswall.databinding.ItemProvinceListBinding

class ProvinceListAdapter : RecyclerView.Adapter<ProvinceListViewHolder>() {

    private var items = ArrayList<ProvinceModel>()

    public lateinit var onClickCallback: (ProvinceModel) -> Unit?

    public fun setItems(items: List<ProvinceModel>) {
        this.items = items as ArrayList<ProvinceModel>
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProvinceListViewHolder {
        return ProvinceListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_province_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProvinceListViewHolder, position: Int) {
        holder.binding.item = items[position]
        holder.itemView.setOnClickListener { onClickCallback.invoke(items[holder.adapterPosition]) }
    }
}

class ProvinceListViewHolder(val binding: ItemProvinceListBinding) :
    RecyclerView.ViewHolder(binding.root)