package ir.kindnesswall.view.profile.blocklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.databinding.ItemBlockedUsersBinding
import ir.kindnesswall.utils.OnItemClickListener

class BlockListAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<ChatContactModel, BlockedUsersViewHolder>(ChatContactModelDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BlockedUsersViewHolder(
            ItemBlockedUsersBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: BlockedUsersViewHolder, position: Int) =
        with(getItem(position)) {
            holder.bind(
                this,
                createOnClickListener(position, getItem(position))
            )
        }

    private fun createOnClickListener(position: Int, item: ChatContactModel) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }
}

class BlockedUsersViewHolder(val binding: ItemBlockedUsersBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatContactModel, listener: View.OnClickListener) =
        with(binding) {
            this.item = item
            this.moreOptionImageView.setOnClickListener(listener)
            executePendingBindings()
        }
}


private class ChatContactModelDiffUtil : DiffUtil.ItemCallback<ChatContactModel>() {
    override fun areItemsTheSame(oldItem: ChatContactModel, newItem: ChatContactModel): Boolean =
        oldItem.chat?.contactId == newItem.chat?.contactId

    override fun areContentsTheSame(oldItem: ChatContactModel, newItem: ChatContactModel): Boolean =
        oldItem.chat?.contactId == newItem.chat?.contactId
}