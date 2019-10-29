package com.numeron.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import com.numeron.common.Identifiable

abstract class PagedBindingAdapter<T : Identifiable<*>, B : ViewDataBinding>(
        @LayoutRes private val layoutId: Int
) : PagedListAdapter<T, BindingHolder<B>>(ItemDiffCallback()) {

    override fun getItemViewType(position: Int) = layoutId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<B> {
        val itemViewDataBinding = DataBindingUtil.inflate<B>(LayoutInflater
                .from(parent.context), viewType, parent, false)
        return BindingHolder(itemViewDataBinding)
    }

}