@file:JvmName("Starter")

package com.numeron.starter

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


inline fun <reified T : Activity> Context.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(intentFor<T>(*extras))
}


inline fun <reified T : Activity> Context.startActivity(option: Bundle?, vararg extras: Pair<String, Any?>) {
    startActivity(intentFor<T>(*extras), option)
}


inline fun <reified T : Service> Context.startService(vararg extras: Pair<String, Any?>) {
    startService(intentFor<T>(*extras))
}


inline fun <reified T : Service> Context.startForegroundService(vararg extras: Pair<String, Any?>) {
    return ContextCompat.startForegroundService(this, intentFor<T>(*extras))
}


inline fun <reified T : Activity> Fragment.startActivity(vararg extras: Pair<String, Any?>) {
    requireContext().startActivity<T>(*extras)
}


inline fun <reified T : Activity> Fragment.startActivity(option: Bundle?, vararg extras: Pair<String, Any?>) {
    requireContext().startActivity<T>(option, *extras)
}


inline fun <reified T : Service> Fragment.startService(vararg extras: Pair<String, Any?>) {
    requireContext().startService<T>(*extras)
}


inline fun <reified T : Service> Fragment.startForegroundService(vararg extras: Pair<String, Any?>) {
    return requireContext().startForegroundService<T>(*extras)
}