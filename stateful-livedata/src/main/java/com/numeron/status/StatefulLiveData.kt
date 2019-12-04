package com.numeron.status

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData

open class StatefulLiveData<T> @JvmOverloads constructor(
        private val empty: String = "没有可用数据",
        private val loading: String = "正在加载数据",
        private val failure: String = "数据加载失败"
) : LiveData<State<out T?>>() {

    @MainThread
    @JvmOverloads
    fun setLoading(loading: String = this.loading, progress: Float = 0f) {
        value = loadingOf(loading, progress)
    }

    @MainThread
    fun setLoading(progress: Float) {
        setLoading(this.loading, progress)
    }

    fun postLoading(progress: Float) {
        postLoading(this.loading, progress)
    }

    @JvmOverloads
    fun postLoading(loading: String = this.loading, progress: Float = 0f) {
        postValue(loadingOf(loading, progress))
    }

    @MainThread
    fun setSuccess(value: T) {
        when {
            value is Collection<*> && value.isEmpty() -> emptyOf(empty)
            value is Array<*> && value.isEmpty() -> emptyOf(empty)
            else -> successOf(value)
        }.let(::setValue)
    }

    fun postSuccess(value: T) {
        when {
            value is Collection<*> && value.isEmpty() -> emptyOf(empty)
            value is Array<*> && value.isEmpty() -> emptyOf(empty)
            else -> successOf(value)
        }.let(::postValue)
    }

    @MainThread
    @JvmOverloads
    fun setEmpty(empty: String = this.empty) {
        value = emptyOf(empty)
    }

    @JvmOverloads
    fun postEmpty(empty: String = this.empty) {
        postValue(emptyOf(empty))
    }

    @MainThread
    fun setFailure(cause: Throwable, failure: String = this.failure) {
        value = failureOf(failure, cause)
    }

    fun postFailure(cause: Throwable, failure: String = this.failure) {
        postValue(failureOf(failure, cause))
    }

    @MainThread
    fun setFailure(cause: Throwable) {
        setFailure(cause, this.failure)
    }

    fun postFailure(cause: Throwable) {
        postFailure(cause, this.failure)
    }

}