package ir.kindnesswall.view.main.conversation.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.local.UserInfoPref
import ir.kindnesswall.data.model.TextMessageBaseModel
import ir.kindnesswall.data.model.TextMessageHeaderModel
import ir.kindnesswall.data.model.TextMessageModel
import ir.kindnesswall.databinding.ItemChatDateHeaderBinding
import ir.kindnesswall.databinding.ItemChatMyselfBinding
import ir.kindnesswall.databinding.ItemChatOthersBinding

class ChatAdapter(val viewModel: ChatViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items = arrayListOf<TextMessageBaseModel>()

    fun setData(data: ArrayList<TextMessageBaseModel>) {
        items = data
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return when {
            items[position] is TextMessageHeaderModel ->
                (items[position] as TextMessageHeaderModel).date.hashCode().toLong()

            items[position] is TextMessageModel ->
                (items[position] as TextMessageModel).id.hashCode().toLong()

            else -> position.toLong()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]

        return if (item is TextMessageModel) {
            if (item.senderId == UserInfoPref.id) 1 else 2
        } else if (item is TextMessageHeaderModel) 3 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> {
                return MyChatsViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_chat_myself,
                        parent,
                        false
                    )
                )
            }

            2 -> {
                return OthersChatsViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_chat_others,
                        parent,
                        false
                    )
                )
            }

            3 -> {
                return ChatDateViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_chat_date_header,
                        parent,
                        false
                    )
                )
            }

            else -> {
                return MyChatsViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_chat_myself,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is MyChatsViewHolder -> {
                holder.binding.item = item as TextMessageModel
            }
            is OthersChatsViewHolder -> {
                holder.binding.item = item as TextMessageModel
                if (!item.ack) {
                    viewModel.sendAckMessage(item.id)
                    item.ack = true
                }
            }
            is ChatDateViewHolder -> {
                holder.binding.item = item as TextMessageHeaderModel
            }
        }
    }
}

class MyChatsViewHolder(var binding: ItemChatMyselfBinding) :
    RecyclerView.ViewHolder(binding.root)


class OthersChatsViewHolder(var binding: ItemChatOthersBinding) :
    RecyclerView.ViewHolder(binding.root)


class ChatDateViewHolder(var binding: ItemChatDateHeaderBinding) :
    RecyclerView.ViewHolder(binding.root)