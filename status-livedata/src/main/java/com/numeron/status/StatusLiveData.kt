package com.numeron.status

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData

class StatusLiveData<T> @JvmOverloads constructor(
        private val empty: String = "没有可用数据",
        private val loading: String = "正在加载数据",
        private val failure: String = "数据加载失败") : LiveData<Status<out T?>>() {

    @MainThread
    @JvmOverloads
    fun setLoading(loading: String = this.loading) {
        value = loadingOf(loading)
    }

    @JvmOverloads
    fun postLoading(loading: String = this.loading) {
        postValue(loadingOf(loading))
    }

    fun setSuccess(value: T) {
        when {
            value is Collection<*> && value.isEmpty() -> emptyOf<T>(empty)
            value is Array<*> && value.isEmpty() -> emptyOf<T>(empty)
            else -> successOf(value)
        }.let(::setValue)
    }

    fun postSuccess(value: T) {
        when {
            value is Collection<*> && value.isEmpty() -> emptyOf<T>(empty)
            value is Array<*> && value.isEmpty() -> emptyOf<T>(empty)
            else -> successOf(value)
        }.let(::postValue)
    }

    @JvmOverloads
    fun setEmpty(empty: String = this.empty) {
        value = emptyOf<T>(empty)
    }

    @JvmOverloads
    fun postEmpty(empty: String = this.empty) {
        postValue(emptyOf<T>(empty))
    }

    @MainThread
    @JvmOverloads
    fun setFailure(failure: String = this.failure) {
        value = emptyOf<T>(failure)
    }

    @JvmOverloads
    fun postFailure(failure: String = this.failure) {
        postValue(failureOf(failure))
    }

}