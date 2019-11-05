package com.numeron.status

import androidx.lifecycle.Observer


class StatefulObserver<T>(
        private val onLoading: (String, Float) -> Unit = { _, _ -> },
        private val onFailure: (String, Throwable) -> Unit = { _, _ -> },
        private val onEmpty: (String) -> Unit = {},
        private val onSuccess: (T) -> Unit = {}
) : Observer<State<out T?>> {

    constructor(callback: StatefulCallback<T>) :
            this(callback::onLoading, callback::onFailure, callback::onEmpty, callback::onSuccess)

    override fun onChanged(state: State<out T?>) {
        state.onLoading(onLoading).onFailure(onFailure).onEmpty(onEmpty).onSuccess(onSuccess)
    }

}


interface StatefulCallback<T> {

    fun onSuccess(value: T)

    fun onFailure(message: String, throwable: Throwable)

    fun onEmpty(message: String)

    fun onLoading(message: String, progress: Float)

}