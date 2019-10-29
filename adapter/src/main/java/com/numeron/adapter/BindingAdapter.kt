package com.numeron.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BindingAdapter<B : ViewDataBinding>(
        private val size: Int = 0,
        @LayoutRes private val layoutId: Int = 0
) : RecyclerView.Adapter<BindingHolder<B>>() {

    override fun getItemCount() = size

    /**
     * 如果该适配器有多种布局，则重写此方法
     * 根据给定的[position]来确定合适的LayoutID
     * @return 布局ID，这个返回值就是[onCreateViewHolder]方法中的viewType参数
     */
    override fun getItemViewType(position: Int) = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<B> {
        val itemViewDataBinding = DataBindingUtil.inflate<B>(
                LayoutInflater.from(
                        parent.context
                ), viewType, parent, false
        )
        return BindingHolder(itemViewDataBinding)
    }

}