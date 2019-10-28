@file:JvmName("ContextUtil")

package com.numeron.util

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast


private class GlobalContext(context: Context) : ContextWrapper(context)


private lateinit var sApplication: Application
private lateinit var sGlobalContext: GlobalContext


fun initContext(application: Application) {
    if (!::sApplication.isInitialized) {
        sApplication = application
    }
    if (!::sGlobalContext.isInitialized) {
        sGlobalContext = GlobalContext(application.applicationContext)
    }
}


val context: Context
    get() = sGlobalContext


val application: Application
    get() = sApplication


/* dp/px 相关工具 */
private val density by lazy {
    context.resources.displayMetrics.density
}

fun dp(value: Int): Int {
    return (density * value + 0.5f).toInt()
}

fun dp(value: Float): Float {
    return density * value + 0.5f
}

fun px2dp(value: Int): Int {
    return (value / density + 0.5f).toInt()
}

fun px2dp(value: Float): Float {
    return value / density + 0.5f
}

/* sp/px 相关工具 */
private val scaledDensity by lazy {
    context.resources.displayMetrics.scaledDensity
}

fun sp(value: Int): Int {
    return (scaledDensity * value + 0.5f).toInt()
}

fun sp(value: Float): Float {
    return scaledDensity * value + 0.5f
}

fun px2sp(value: Int): Int {
    return (value / scaledDensity + 0.5f).toInt()
}

fun px2sp(value: Float): Float {
    return value / scaledDensity + 0.5f
}

/* Toast 相关 */

private val globalToast by lazy {
    Toast.makeText(context, null, Toast.LENGTH_SHORT)
}

/**
 * 及时的、尽快的显示一个Toast，多次调用此方法的Toast会被后调用的覆盖
 * @param text String 要显示的文本
 * @return Toast 单例的Toast对象
 */
fun timelyToast(text: String): Toast {
    globalToast.duration = Toast.LENGTH_SHORT
    globalToast.setText(text)
    globalToast.show()
    return globalToast
}

/**
 * 同上，但是显示的时间稍长一点
 */
fun timelyLongToast(text: String): Toast {
    globalToast.duration = Toast.LENGTH_LONG
    globalToast.setText(text)
    globalToast.show()
    return globalToast
}

inline fun <reified T : Context> T.toast(text: String): Toast {
    return Toast.makeText(this, text, Toast.LENGTH_SHORT).apply(Toast::show)
}

inline fun <reified T : Context> T.longToast(text: String): Toast {
    return Toast.makeText(this, text, Toast.LENGTH_LONG).apply(Toast::show)
}