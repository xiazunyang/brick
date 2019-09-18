package com.numeron.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * 默认情况下，长度不会变、布局单一的RecyclerView适配器
 * 除非如果该列表的长度是可变的，需要重写[getItemCount]方法，此时[size]参数可忽略
 * 如果该适配器有多种布局，需要重写[getItemViewType]方法，此时[layoutId]参数可忽略
 * @property size Int 表示一个List的长度
 * @property layoutId Int  LayoutID
 */
abstract class LiteAdapter(
        private val size: Int = 0,
        @LayoutRes private val layoutId: Int = 0) : RecyclerView.Adapter<ViewHolder>() {

    /**
     * 如果该适配器有多种布局，则重写此方法
     * 根据给定的[position]来确定合适的LayoutID
     * @return 布局ID，这个返回值就是[onCreateViewHolder]方法中的viewType参数
     */
    override fun getItemViewType(position: Int) = layoutId

    override fun getItemCount() = size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            LayoutInflater
                    .from(parent.context)
                    .inflate(viewType, parent, false)
                    .let(::ViewHolder)

}