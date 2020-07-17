package ir.kindnesswall.view.main.more.userlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.data.model.user.User
import ir.kindnesswall.databinding.ItemUsersBinding

class UserListAdapter(private var onItemClick: (user: User) -> Unit) : RecyclerView.Adapter<ViewHolder>() {
    val mUserList = ArrayList<User>()

    override fun getItemId(position: Int): Long {
        return mUserList[position].id ?: position.toLong()
    }

    override fun getItemCount() = mUserList.size

    // In RecyclerView.Adapter
    fun submitList(newList: List<User>) {
        DiffUtil.calculateDiff(UserDiffUtil(this.mUserList, newList)).dispatchUpdatesTo(this)
        this.mUserList.clear()
        this.mUserList.addAll(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(mUserList[position]) { holder.binding.item = this }
        holder.itemView.setOnClickListener { onItemClick.invoke(mUserList[position]) }
    }
}

class ViewHolder(val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root)


private class UserDiffUtil(val oldList: List<User>, val newList: List<User>) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}