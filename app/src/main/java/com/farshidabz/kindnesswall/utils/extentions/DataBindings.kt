package com.farshidabz.kindnesswall.utils.extentions

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions.circleCropTransform
import com.farshidabz.kindnesswall.utils.imageloader.loadImage


/**
 * Created by Amir farshid abazari since 25/10/19
 *
 * Usage:
 *
 * How to call:
 *
 * Useful parameter:
 *
 */

@BindingAdapter("app:srcCompat")
fun bindSrcCompat(imageView: ImageView, drawable: Drawable) {
    imageView.setImageDrawable(drawable)
}

@BindingAdapter("app:srcCompatWithId")
fun bindSrcCompatWithId(imageView: ImageView, drawableId: Int) {
    imageView.setImageResource(drawableId)
}

@BindingAdapter("app:backgroundColorWithId")
fun bindbackgroundColorWithId(view: View, backgroundColorId: Int) {
    view.setBackgroundResource(backgroundColorId)
}

@BindingAdapter("srcUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if (imageUrl == null) return
    loadImage(imageUrl, view)
}

@BindingAdapter("loadImageFromList")
fun loadImageFromList(view: ImageView, imageList: List<String>?) {
    var imageUrl = ""

    if (!imageList.isNullOrEmpty()) {
        imageUrl = imageList[0]
    }

    loadImage(imageUrl, view)
}

@BindingAdapter("srcCircleUrl")
fun loadCircleImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let {
        loadImage(
            imageUrl,
            view,
            options = circleCropTransform()
        )
    }
}

@BindingAdapter("backgroundDrawable")
fun setBackground(view: View, drawable: Drawable) {
    view.background = drawable
}

@BindingAdapter("isVisible")
fun loadCircleImage(view: View, isVisible: Boolean) {
    if (isVisible) view.visibility = View.VISIBLE else view.visibility = View.GONE
}

@BindingAdapter("color")
fun setColor(textView: TextView, color: Int) {
    textView.setTextColor(color)
}

@BindingAdapter("navigateUpOnClick")
fun navigateUp(view: View, isEnabled: Boolean = false) {
    if (isEnabled) view.setOnClickListener {
        view.context?.let {
            (it as AppCompatActivity).onBackPressed()
        }
    }
}

@BindingAdapter("drawableTint")
fun TextView.setTintToDrawable(color: Int) {
    postDelayed({ setDrawableTintColor(color) }, 50)
}

@BindingAdapter("isBold")
fun setBold(view: TextView, isBold: Boolean) {
    if (isBold) {
        view.setTypeface(null, Typeface.BOLD)
    } else {
        view.setTypeface(null, Typeface.NORMAL)
    }
}

@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: String?) {
    if (imageUri == null) {
        view.setImageURI(null)
    } else {
        view.setImageURI(Uri.parse(imageUri))
    }
}

@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: Uri) {
    view.setImageURI(imageUri)
}

@BindingAdapter("android:src")
fun setImageDrawable(view: ImageView, drawable: Drawable?) {
    if (drawable == null) return
    view.setImageDrawable(drawable)
}

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("nestedScrollingEnabled")
fun setNestedScrollingEnabled(recyclerView: RecyclerView, value: Boolean) {
    recyclerView.isNestedScrollingEnabled = value
}

@BindingAdapter("htmlText")
fun setTextHtml(textView: TextView, value: String) {
    textView.text = HtmlCompat.fromHtml(value, HtmlCompat.FROM_HTML_MODE_LEGACY)
}