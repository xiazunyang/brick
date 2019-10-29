package com.numeron.wandroid.other

import android.util.Log


inline fun <reified T> T.vLog(any: Any, throwable: Throwable? = null) {
    Log.v(T::class.java.simpleName, any as? String ?: any.toString(), throwable)
}


inline fun <reified T> T.dLog(any: Any, throwable: Throwable? = null) {
    Log.d(T::class.java.simpleName, any as? String ?: any.toString(), throwable)
}


inline fun <reified T> T.iLog(any: Any, throwable: Throwable? = null) {
    Log.i(T::class.java.simpleName, any as? String ?: any.toString(), throwable)
}


inline fun <reified T> T.wLog(any: Any, throwable: Throwable? = null) {
    Log.w(T::class.java.simpleName, any as? String ?: any.toString(), throwable)
}


inline fun <reified T> T.eLog(any: Any, throwable: Throwable? = null) {
    Log.e(T::class.java.simpleName, any as? String ?: any.toString(), throwable)
}