package com.numeron.adapter

import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("UNCHECKED_CAST")
fun <T, VH : RecyclerView.ViewHolder> PagedListAdapter<T, VH>.getDiffer(): AsyncPagedListDiffer<T> {
    val mDifferField = PagedListAdapter::class.java.getDeclaredField("mDiffer")
    mDifferField.isAccessible = true
    return mDifferField.get(this) as AsyncPagedListDiffer<T>
}