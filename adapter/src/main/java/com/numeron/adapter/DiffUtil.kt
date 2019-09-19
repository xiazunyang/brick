package com.numeron.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


/**
 * 当RecyclerView的列表发生变化时，使用此工具来对比差异，并且处理动画
 */
open class DiffUtil<T>(
        protected val oldList: List<T>,
        protected val newList: List<T>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    fun <VH : RecyclerView.ViewHolder> dispatchUpdatesTo(adapter: RecyclerView.Adapter<VH>) {
        DiffUtil.calculateDiff(this).dispatchUpdatesTo(adapter)
    }

    fun dispatchUpdatesTo(recyclerView: RecyclerView) {
        dispatchUpdatesTo(recyclerView.adapter ?: return)
    }

}