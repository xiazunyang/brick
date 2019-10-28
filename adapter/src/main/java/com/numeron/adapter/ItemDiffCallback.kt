package com.numeron.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.numeron.common.Identifiable

class ItemDiffCallback<T : Identifiable<*>> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem == newItem
    }

}