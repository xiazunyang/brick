package com.numeron.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import com.numeron.common.Identifiable

/**
 * 用于Paging的Adapter，需要传入[layoutId]作为布局
 * 如果该适配器有多种布局，需要重写[getItemViewType]方法，此时[layoutId]参数可忽略
 */
abstract class PagedAdapter<T : Identifiable<*>>(
        @LayoutRes private val layoutId: Int = 0
) : PagedListAdapter<T, ViewHolder>(ItemDiffCallback()) {

    /**
     * 如果该适配器有多种布局，则重写此方法
     * 根据给定的[position]来确定合适的LayoutID
     * @return 布局ID，这个返回值就是[onCreateViewHolder]方法中的viewType参数
     */
    override fun getItemViewType(position: Int) = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayoutInflater
                    .from(parent.context)
                    .inflate(viewType, parent, false)
                    .let(::ViewHolder)

}