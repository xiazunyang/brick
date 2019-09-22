package com.numeron.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter

abstract class PagedBindingAdapter<T, B : ViewDataBinding>(
        @LayoutRes private val layoutId: Int
) : PagedListAdapter<T, DataBindingViewHolder<B>>(ItemDiffCallback()) {

    override fun getItemViewType(position: Int) = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<B> {
        val itemViewDataBinding = DataBindingUtil.inflate<B>(LayoutInflater
                .from(parent.context), viewType, parent, false)
        return DataBindingViewHolder(itemViewDataBinding)
    }

}