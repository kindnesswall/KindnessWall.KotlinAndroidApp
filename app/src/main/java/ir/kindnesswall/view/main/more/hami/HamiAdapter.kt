package ir.kindnesswall.view.main.more.hami

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ir.kindnesswall.R
import ir.kindnesswall.data.model.hami.HamiModel
import ir.kindnesswall.utils.extentions.loadCircleImage
import kotlinx.android.synthetic.main.item_hami.view.*

class HamiAdapter(val data: List<HamiModel>?) : RecyclerView.Adapter<HamiAdapter.Holder>() {

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtName: TextView = itemView.txtName
        private val txtDescription: TextView = itemView.txtDescription
        private val img: ImageView = itemView.img
        fun bind(position: Int) {
            val item = data?.get(position)
            txtName.text = item?.name
            if (item?.description != null && item.description.isNotEmpty()) {
                txtDescription.visibility = View.VISIBLE
                txtDescription.text = item.description
            }
            loadCircleImage(img, item?.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_hami, parent, false)
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }
}