package com.numeron.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

/**
 * 仿Anko的启动Activity的写法
 */

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<String, *>) {
    startActivity(Intent(this, T::class.java).putExtras(bundleOf(*params)))
}


inline fun <reified T : Activity> Fragment.startActivity(vararg params: Pair<String, *>) {
    requireContext().startActivity<T>(*params)
}


inline fun <reified T : Activity> Context.startActivity(options: Bundle, vararg params: Pair<String, *>) {
    startActivity(Intent(this, T::class.java).putExtras(bundleOf(*params)), options)
}


inline fun <reified T : Activity> Fragment.startActivity(options: Bundle, vararg params: Pair<String, *>) {
    requireContext().startActivity<T>(options, *params)
}

