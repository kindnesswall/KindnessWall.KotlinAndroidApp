package com.farshidabz.kindnesswall.utils.widgets.photoslider.viewtypes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.utils.imageloader.loadImage

/**
 * Created by farshid.abazari since 1/28/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class PhotoSliderAdapter(private var isZoomable: Boolean) :
    RecyclerView.Adapter<PhotoSliderViewHolder>() {

    private var images = ArrayList<String>()

    private var clickListener: ((Int) -> Unit)? = null

    fun setOnPhotoClickListener(clickListener: ((Int) -> Unit)? = null) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoSliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_photo_slider, parent, false)

        return PhotoSliderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: PhotoSliderViewHolder, position: Int) {
        holder.image.isZoomable = isZoomable

        loadImage(
            images[position],
            holder.image,
            0,
            null,
            holder.progressBar,
            diskCacheStrategy = DiskCacheStrategy.ALL)

        holder.itemView.setOnClickListener {
            clickListener?.invoke(holder.adapterPosition)
        }
    }

    fun setItems(images: ArrayList<String>) {
        this.images = images
        notifyDataSetChanged()
    }
}