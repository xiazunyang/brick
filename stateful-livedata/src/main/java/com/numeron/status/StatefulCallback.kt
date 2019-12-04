package com.numeron.status


interface StatefulCallback<T> {

    fun onSuccess(value: T)
    fun onEmpty(message: String)
    fun onLoading(message: String, progress: Float)
    fun onFailure(message: String, cause: Throwable)

}