package com.farshidabz.kindnesswall.utils.widgets.photoslider.viewtypes

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.R
import com.github.chrisbanes.photoview.PhotoView


/**
 * Created by farshid.abazari since 1/23/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

class PhotoSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var image: PhotoView = itemView.findViewById(R.id.slider_appImageView_image)
    var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
}