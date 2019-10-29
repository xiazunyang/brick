package com.numeron.wandroid.other

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter(value = ["app:bindDate", "app:formatPattern"], requireAll = false)
fun TextView.bindDate(date: Any, formatPattern: String?) {
    val format = formatPattern ?: "yyyy-MM-dd HH:mm:ss"
    val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
    text = simpleDateFormat.format(date)
}