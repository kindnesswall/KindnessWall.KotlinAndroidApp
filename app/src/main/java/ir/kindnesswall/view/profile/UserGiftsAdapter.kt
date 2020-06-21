package ir.kindnesswall.view.profile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.local.dao.catalog.GiftModel
import ir.kindnesswall.databinding.ItemProfileGiftsBinding
import ir.kindnesswall.utils.OnItemClickListener

class UserGiftsAdapter(private val onItemClickListener: OnItemClickListener) :
    ListAdapter<GiftModel, CatalogViewHolder>(CatalogDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CatalogViewHolder(
            ItemProfileGiftsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) =
        with(getItem(position)) {
            holder.bind(
                this,
                createOnClickListener(position, getItem(position))
            )
        }

    private fun createOnClickListener(position: Int, item: GiftModel) =
        View.OnClickListener {
            onItemClickListener.onItemClicked(position, item)
        }
}

class CatalogViewHolder(val binding: ItemProfileGiftsBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(item: GiftModel, listener: View.OnClickListener) =
        with(binding) {
            this.item = item
            clickListener = listener

            setSituationText(item)

            executePendingBindings()
        }

    private fun setSituationText(item: GiftModel) {
        item.let {
            if (!it.isReviewed) {
                binding.giftStatusFlag.visibility = View.VISIBLE
                binding.giftStatusFlag.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.giftStatusFlag.context,
                        R.color.situation_yellow_color
                    )
                )
                return
            }

            if (it.isReviewed && !it.isRejected && it.donatedToUserId != null && it.donatedToUserId!! > 0) {
                binding.giftStatusFlag.visibility = View.GONE
                return
            }

            if (it.isReviewed && it.isRejected) {
                binding.giftStatusFlag.visibility = View.VISIBLE
                binding.giftStatusFlag.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.giftStatusFlag.context,
                        R.color.red
                    )
                )
                return
            }

            if (it.isReviewed) {
                binding.giftStatusFlag.visibility = View.VISIBLE
                binding.giftStatusFlag.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.giftStatusFlag.context,
                        R.color.colorPrimary
                    )
                )
                return
            }

            binding.giftStatusFlag.visibility = View.GONE
        }
    }
}


private class CatalogDiffUtil : DiffUtil.ItemCallback<GiftModel>() {
    override fun areItemsTheSame(oldItem: GiftModel, newItem: GiftModel): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: GiftModel, newItem: GiftModel): Boolean =
        oldItem == newItem
}