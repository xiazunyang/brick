@file:JvmName("Intents")

package com.numeron.starter

import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf


fun intentOf(vararg extras: Pair<String, Any?>): Intent {
    return Intent().putExtras(bundleOf(*extras))
}


fun <T> Context.intentFor(clazz: Class<T>, vararg extras: Pair<String, Any?>): Intent {
    return Intent(this, clazz).putExtras(bundleOf(*extras))
}


inline fun <reified T> Context.intentFor(vararg extras: Pair<String, Any?>): Intent {
    return intentFor(T::class.java, *extras)
}