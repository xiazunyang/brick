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
        //获取当前View的位置
        val position = parent.getChildAdapterPosition(view) - headerCount
        //获取有多少个View
        val itemCount = parent.adapter?.itemCount ?: 0
        //获取有多少列
        val columnCount = getSpanCount(parent)
        //获取有多少行
        val rowCount = getRowNumber(itemCount, columnCount)
        //当前是第几列
        val column = position % columnCount
        //当前是第几行
        val row = getRowNumber(position + 1, columnCount)
        //计算平均间隔
        val average = space / columnCount
        rect.left = (columnCount - column) * average
        rect.top = space
        rect.right = (column + 1) * average
        rect.bottom = if (row == rowCount) space else 0
    }

    private fun getRowNumber(countOrPosition: Int, spanCount: Int): Int {
        return countOrPosition / spanCount + if (countOrPosition % spanCount > 0) 1 else 0
    }

    private fun getLinearLayoutOrientation(parent: RecyclerView): Int {
        return (parent.layoutManager as LinearLayoutManager).orientation
    }

    private fun setOutRectByLinear(rect: Rect, view: View, parent: RecyclerView) {
        val orientation = getLinearLayoutOrientation(parent)
        val isFirstView = parent.getChildLayoutPosition(view) - headerCount == 0
        val top = if (isFirstView || orientation == LinearLayoutManager.HORIZONTAL) space else 0
        val left = if(isFirstView || orientation == LinearLayoutManager.VERTICAL) space else 0
        rect.top = top
        rect.left = left
        rect.right = space
        rect.bottom = space
    }

}