@file:JvmName("AttributeBindingAdapter")
@file:Suppress("unused")

package com.numeron.wan.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter(value = ["app:bindDate", "app:formatPattern"], requireAll = false)
fun TextView.bindDate(date: Any, formatPattern: String?) {
    val format = formatPattern ?: "yyyy-MM-dd HH:mm:ss"
    val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
    text = simpleDateFormat.format(date)
}


@BindingAdapter(value = ["app:onRefresh"])
fun SwipeRefreshLayout.onRefresh(l: SwipeRefreshLayout.OnRefreshListener) {
    setOnRefreshListener(l)
}