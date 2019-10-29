@file:JvmName("Starter")

package com.numeron.starter

import android.app.Activity
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.os.Bundle


inline fun <reified T : Activity> Context.startActivity(vararg extras: Pair<String, Any?>) {
    startActivity(intentFor<T>(*extras))
}


inline fun <reified T : Activity> Context.startActivity(option: Bundle, vararg extras: Pair<String, Any?>) {
    startActivity(intentFor<T>(*extras), option)
}


inline fun <reified T : Service> Context.startService(vararg extras: Pair<String, Any?>): ComponentName {
    return startService(intentFor<T>(*extras))!!
}


inline fun <reified T : Service> Context.startForegroundService(vararg extras: Pair<String, Any?>): ComponentName {
    return startForegroundService(intentFor<T>(*extras))!!
}