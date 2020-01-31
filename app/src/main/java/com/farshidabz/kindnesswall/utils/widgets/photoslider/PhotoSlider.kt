package com.farshidabz.kindnesswall.utils.widgets.photoslider

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.farshidabz.kindnesswall.R
import com.farshidabz.kindnesswall.utils.extentions.getSnapPosition
import com.farshidabz.kindnesswall.utils.widgets.photoslider.viewtypes.PhotoSliderAdapter
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator


/**
 * Created by farshid.abazari since 1/16/19
 *
 * Usage: A view to show photos in a slider in other views
 *
 * How to call: create a view on your layout with photoSlider tag and set images programmatically
 * then call show method
 */

class PhotoSlider : RelativeLayout {

    private var pageChangeCallback: ((Int) -> Unit)? = null

    private var imagesUrl = ArrayList<String>()

    private lateinit var adapter: PhotoSliderAdapter

    lateinit var photoSliderRecyclerView: RecyclerView
    lateinit var recyclerPagerIndicator: IndefinitePagerIndicator

    private var zoomable: Boolean = false
    private var indicatorVisibility: Boolean = true
    private var overrideImageSize: Boolean = false

    private var snapPosition = RecyclerView.NO_POSITION

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun show(images: List<String>?) {
        this.imagesUrl.clear()

        images?.let {
            this.imagesUrl.addAll(it)

            if (imagesUrl.size <= 1) {
                recyclerPagerIndicator.visibility = View.INVISIBLE
            } else {
                recyclerPagerIndicator.visibility = View.VISIBLE
            }
        }

        addDataToRecyclerView()
        checkEmptyPage()
    }

    fun setOnPhotoClickListener(clickListener: ((Int) -> Unit)? = null) {
        if (::adapter.isInitialized) {
            adapter.setOnPhotoClickListener(clickListener)
        }
    }

    fun setOnPageChangeListener(pageChangeCallback: ((Int) -> Unit)? = null) {
        this.pageChangeCallback = pageChangeCallback
    }

    fun initialize(
        zoomable: Boolean = false,
        showIndicator: Boolean = true,
        clickListener: ((Int) -> Unit)? = null,
        pageChangeCallback: ((Int) -> Unit)? = null
    ): PhotoSlider {
        this.zoomable = zoomable
        this.indicatorVisibility = showIndicator

        inflate(context, R.layout.photo_slider, this)
        setupRecyclerView()
        setupIndicator()

        this.pageChangeCallback = pageChangeCallback
        adapter.setOnPhotoClickListener(clickListener)

        return this
    }

    private fun setupRecyclerView() {
        adapter = PhotoSliderAdapter(zoomable)
        photoSliderRecyclerView = findViewById(R.id.photoSliderRecyclerView)

        photoSliderRecyclerView.adapter = adapter
        photoSliderRecyclerView.setHasFixedSize(true)

        photoSliderRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        photoSliderRecyclerView.onFlingListener = null

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(photoSliderRecyclerView)

        photoSliderRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                maybeNotifySnapPositionChange(recyclerView, snapHelper)
            }
        })
    }

    private fun maybeNotifySnapPositionChange(
        recyclerView: RecyclerView,
        snapHelper: PagerSnapHelper
    ) {
        val snapPosition = snapHelper.getSnapPosition(recyclerView)
        val snapPositionChanged = this.snapPosition != snapPosition
        if (snapPositionChanged) {
            pageChangeCallback?.invoke(snapPosition)
            this.snapPosition = snapPosition
        }
    }

    private fun setupIndicator() {
        recyclerPagerIndicator = findViewById(R.id.recyclerPagerIndicator)
        recyclerPagerIndicator.attachToRecyclerView(photoSliderRecyclerView)

        when (indicatorVisibility) {
            true -> recyclerPagerIndicator.visibility = View.VISIBLE
            false -> recyclerPagerIndicator.visibility = View.INVISIBLE
        }
    }

    private fun addDataToRecyclerView() {
        adapter.setItems(imagesUrl)
    }

    private fun checkEmptyPage() {
        if (adapter.itemCount == 0) {
            findViewById<LinearLayout>(R.id.photoSliderPlaceHolder).visibility = View.VISIBLE
        } else {
            findViewById<LinearLayout>(R.id.photoSliderPlaceHolder).visibility = View.GONE
        }
    }

    fun scrollToPosition(selectedImageIndex: Int) {
        photoSliderRecyclerView.scrollToPosition(selectedImageIndex)
    }

    fun getItemCount() = imagesUrl.size
}