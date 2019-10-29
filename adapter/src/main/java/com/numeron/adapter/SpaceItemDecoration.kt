package com.numeron.adapter

import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class SpaceItemDecoration(
        private val space: Int,
        private val headerCount: Int = 0) : RecyclerView.ItemDecoration() {

    private fun getSpanCount(recyclerView: RecyclerView): Int {
        return when (val manager = recyclerView.layoutManager) {
            is StaggeredGridLayoutManager -> manager.spanCount
            is GridLayoutManager -> manager.spanCount
            else -> 0
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (parent.layoutManager) {
            is StaggeredGridLayoutManager -> setOutRectByGrid(outRect, view, parent)
            is GridLayoutManager -> setOutRectByGrid(outRect, view, parent)
            is LinearLayoutManager -> setOutRectByLinear(outRect, view, parent)
            else -> Log.e("SpaceItemDecoration", "没有设置LayoutManager，或者是不支持的LayoutManager。")
        }
    }

    private fun setOutRectByGrid(rect: Rect, view: View, parent: RecyclerView) {
        val position = parent.getChildAdapterPosition(view) - headerCount
        val spanCount = getSpanCount(parent)
        val column = position % spanCount
        rect.left = space
        rect.bottom = space
        rect.top = if(position < spanCount) space else 0
        rect.right = if(column == spanCount - 1) space else 0
    }

    private fun setOutRectByLinear(rect: Rect, view: View, parent: RecyclerView) {
        rect.top = if (parent.getChildLayoutPosition(view) - headerCount == 0) space else 0
        rect.left = space
        rect.right = space
        rect.bottom = space
    }

}