package ir.kindnesswall.view.main.conversation.chat.todonategifts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.databinding.ItemGiftsToDonateBinding

class ToDonateListAdapter : RecyclerView.Adapter<ToDonateListViewHolder>() {
    private lateinit var listener: (GiftModel) -> Unit

    private var items = ArrayList<GiftModel>()

    fun setItems(items: ArrayList<GiftModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (GiftModel) -> Unit) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDonateListViewHolder {
        return ToDonateListViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_gifts_to_donate,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ToDonateListViewHolder, position: Int) {
        val item = items[position]

        holder.binding.item = item
        holder.itemView.setOnClickListener { listener.invoke(item) }
    }
}

class ToDonateListViewHolder(val binding: ItemGiftsToDonateBinding) :
    RecyclerView.ViewHolder(binding.root)