package com.numeron.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class SpaceItemDecoration(private val rect: Rect) : RecyclerView.ItemDecoration() {

    constructor(space: Int) : this(Rect(space, space, space, space))

    constructor(left: Int, top: Int, right: Int, bottom: Int) : this(Rect(left, top, right, bottom))

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(rect)
    }

}