package com.numeron.status

import androidx.lifecycle.LiveData
import com.numeron.common.State

class StatefulLiveData<T> @JvmOverloads constructor(
        private val empty: String = "没有数据",
        private val loading: String = "正在加载",
        private val success: String = "加载成功",
        private val failure: String = "加载失败"
) : LiveData<Stateful<T>>() {

    private val impl: StatefulImpl<T>
        get() = super.getValue() as? StatefulImpl<T> ?: StatefulImpl(State.Non)

    val value: T?
        @JvmName("value")
        get() = (super.getValue() as? StatefulImpl)?.success

    val requireValue: T
        @JvmName("requireValue")
        get() = value!!

    fun postLoading(progress: Float) {
        postLoading(this.loading, progress)
    }

    @JvmOverloads
    fun postLoading(message: String = this.loading, progress: Float = -1f) {
        postValue(impl.copy(state = State.Loading, progress = progress, message = message))
    }

    fun postSuccess(value: T, message: String? = this.success) {
        postValue(impl.copy(state = State.Success, success = value, message = message))
    }

    @JvmOverloads
    fun postEmpty(empty: String = this.empty) {
        postValue(impl.copy(state = State.Empty, message = empty))
    }

    fun postFailure(cause: Throwable, message: String = this.failure) {
        postValue(impl.copy(state = State.Failure, failure = cause, message = message))
    }

    fun postFailure(cause: Throwable) {
        postFailure(cause, failure)
    }

    fun postMessage(message: String) {
        postValue(impl.copy(state = State.Non, message = message))
    }

}