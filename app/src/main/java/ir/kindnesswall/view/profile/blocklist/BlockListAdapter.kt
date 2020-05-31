package ir.kindnesswall.view.profile.blocklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.databinding.ItemBlockedUsersBinding
import ir.kindnesswall.utils.OnItemClickListener

class BlockListAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<BlockedUsersViewHolder>() {

    private var items: List<ChatContactModel> = arrayListOf()

    fun submitList(items: List<ChatContactModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return items[position].contactProfile?.id ?: position.toLong()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BlockedUsersViewHolder(
            ItemBlockedUsersBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: BlockedUsersViewHolder, position: Int) {
        val item = items[position]

        holder.binding.item = item

        holder.binding.moreOptionImageView.setOnClickListener {
            onItemClickListener.onItemClicked(holder.adapterPosition, item)
        }
    }
}

class BlockedUsersViewHolder(val binding: ItemBlockedUsersBinding) :
    RecyclerView.ViewHolder(binding.root)
