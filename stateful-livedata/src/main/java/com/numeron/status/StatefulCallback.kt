package com.numeron.status


interface StatefulCallback<T> {

    fun onSuccess(value: T, message: String?)
    fun onLoading(progress: Float, message: String?)
    fun onFailure(cause: Throwable, message: String?)
    fun onMessage(message: String)
    fun onEmpty(message: String)

}