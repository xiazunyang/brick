package com.numeron.status

import com.numeron.common.State

internal data class StatefulImpl<T>(
        override val state: State,
        internal val success: T? = null,
        internal val failure: Throwable? = null,
        internal val progress: Float = -1f,
        internal val message: String? = null
) : Stateful<T> {

    override fun onSuccess(c: (T, String?) -> Unit): Stateful<T> {
        if (state == State.Success && success != null) {
            c(success, message)
        }
        return this
    }

    override fun onFailure(c: (Throwable, String?) -> Unit): Stateful<T> {
        if (state == State.Failure && failure != null) {
            c(failure, message)
        }
        return this
    }

    override fun onLoading(c: (Float, String?) -> Unit): Stateful<T> {
        if (state == State.Loading && progress > 0f) {
            c(progress, message)
        }
        return this
    }

    override fun onEmpty(c: (String) -> Unit): Stateful<T> {
        if (state == State.Empty && message != null) {
            c(message)
        }
        return this
    }

    override fun onMessage(c: (String) -> Unit): Stateful<T> {
        if (state == State.Non && message != null) {
            c(message)
        }
        return this
    }

}
