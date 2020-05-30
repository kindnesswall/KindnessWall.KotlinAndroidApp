package ir.kindnesswall.utils.helper

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerViewScrollListener(private var layoutManager: RecyclerView.LayoutManager) :
    RecyclerView.OnScrollListener() {

    var PAGE_SIZE = 2
    var VISIBLE_THRESHOLD = 10

    var isLoading = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        val firstVisibleItemPosition: Int = if (layoutManager is LinearLayoutManager) {
            (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        } else {
            (layoutManager as GridLayoutManager).findFirstVisibleItemPosition()
        }

        onScrolled(firstVisibleItemPosition)

        if (!isLoading) {
            if ((firstVisibleItemPosition + VISIBLE_THRESHOLD) >= totalItemCount - visibleItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE / 2
            ) {
                onLoadMore()
            }
        }
    }

    abstract fun onLoadMore()

    abstract fun onScrolled(position: Int)
}