package ir.kindnesswall.view.main.conversation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.data.model.ChatContactModel
import ir.kindnesswall.databinding.ItemConversationsBinding
import ir.kindnesswall.utils.OnItemClickListener

class ConversationListAdapter(private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<ConversationViewHolder>() {

    private var items = listOf<ChatContactModel>()

    fun submitList(items: List<ChatContactModel>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder =
        ConversationViewHolder(
            ItemConversationsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val item = items[position]
        with(item) {
            holder.bind(
                this,
                createOnClickListener(position, item)
            )
        }
    }

    private fun createOnClickListener(position: Int, item: ChatContactModel) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }
}

class ConversationViewHolder(val binding: ItemConversationsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ChatContactModel, listener: View.OnClickListener) =
        with(binding) {
            this.item = item
            clickListener = listener
            executePendingBindings()
        }
}