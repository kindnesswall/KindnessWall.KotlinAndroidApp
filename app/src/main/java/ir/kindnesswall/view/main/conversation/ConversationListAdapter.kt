package ir.kindnesswall.view.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.data.model.ConversationModel
import ir.kindnesswall.databinding.ItemConversationsBinding
import ir.kindnesswall.utils.OnItemClickListener

class ConversationListAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<ConversationModel, ConversationViewHolder>(ConversationDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder =
        ConversationViewHolder(
            ItemConversationsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) =
        with(getItem(position)) {
            holder.bind(
                this,
                createOnClickListener(position, getItem(position))
            )
        }

    private fun createOnClickListener(position: Int, item: ConversationModel) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }
}


class ConversationViewHolder(val binding: ItemConversationsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ConversationModel, listener: View.OnClickListener) =
        with(binding) {
            this.item = item
            clickListener = listener
            executePendingBindings()
        }
}


private class ConversationDiffUtil : DiffUtil.ItemCallback<ConversationModel>() {
    override fun areItemsTheSame(oldItem: ConversationModel, newItem: ConversationModel): Boolean =
        oldItem.chat?.chatId == newItem.chat?.chatId

    override fun areContentsTheSame(
        oldItem: ConversationModel,
        newItem: ConversationModel
    ): Boolean =
        oldItem == newItem
}