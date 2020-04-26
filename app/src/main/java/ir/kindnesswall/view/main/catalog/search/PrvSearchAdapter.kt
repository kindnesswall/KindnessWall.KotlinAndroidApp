package ir.kindnesswall.view.main.catalog.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.databinding.ItemPrvSearchBinding
import ir.kindnesswall.utils.OnItemClickListener

class PrvSearchAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<String, PrvSearchViewHolder>(PrvSearchDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PrvSearchViewHolder(
            ItemPrvSearchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: PrvSearchViewHolder, position: Int) =
        with(getItem(position)) {
            holder.bind(
                this,
                createOnClickListener(position, getItem(position))
            )
        }

    private fun createOnClickListener(position: Int, item: String) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }
}

class PrvSearchViewHolder(val binding: ItemPrvSearchBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: String, listener: View.OnClickListener) =
        with(binding) {
            this.item = item
            clickListener = listener
            executePendingBindings()
        }
}

private class PrvSearchDiffUtil : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
        oldItem == newItem
}