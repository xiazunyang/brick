package com.numeron.stateful.livedata

interface StatefulCallback<T> {

    fun onSuccess(value: T)
    fun onLoading(message: String, progress: Float)
    fun onFailure(message: String, cause: Throwable)
    fun onMessage(message: String): Unit = Unit

}