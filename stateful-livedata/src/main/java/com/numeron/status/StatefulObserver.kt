package com.numeron.status

import androidx.lifecycle.Observer


class StatefulObserver<T>(private val callback: StatefulCallback<T>) : Observer<State<out T?>> {

    constructor(
            onSuccess: (T) -> Unit = {},
            onFailure: (String, Throwable) -> Unit = { _, _ -> },
            onLoading: (String, Float) -> Unit = { _, _ -> },
            onEmpty: (String) -> Unit = {}
    ) : this(object : StatefulCallback<T> {
        override fun onSuccess(value: T) = onSuccess(value)
        override fun onEmpty(message: String) = onEmpty(message)
        override fun onFailure(message: String, cause: Throwable) = onFailure(message, cause)
        override fun onLoading(message: String, progress: Float) = onLoading(message, progress)
    })

    override fun onChanged(state: State<out T?>) {
        state.onFailure(callback::onFailure)
                .onSuccess(callback::onSuccess)
                .onLoading(callback::onLoading)
                .onEmpty(callback::onEmpty)
    }

}
