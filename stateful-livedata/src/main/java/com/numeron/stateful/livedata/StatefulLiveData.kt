package com.numeron.stateful.livedata

import androidx.lifecycle.LiveData
import com.numeron.common.State

class StatefulLiveData<T> @JvmOverloads constructor(
        private val loading: String = "正在加载",
        private val failure: String = "加载失败"
) : LiveData<Stateful<T>>() {

    private val valueOrDefault: StatefulImpl<T>
        get() = getValue() as? StatefulImpl ?: StatefulImpl(State.Empty)

    val value: T?
        @JvmName("value")
        get() = (getValue() as? StatefulImpl)?.success

    val requireValue: T
        @JvmName("requireValue")
        get() = value!!

    fun postLoading(progress: Float) {
        postLoading(this.loading, progress)
    }

    @JvmOverloads
    fun postLoading(message: String = this.loading, progress: Float = -1f) {
        postValue(valueOrDefault.copy(state = State.Loading, progress = progress, message = message))
    }

    fun postSuccess(value: T) {
        postValue(valueOrDefault.copy(state = State.Success, success = value))
    }

    fun postFailure(cause: Throwable, message: String = this.failure) {
        postValue(valueOrDefault.copy(state = State.Failure, failure = cause, message = message))
    }

    fun postFailure(cause: Throwable) {
        postFailure(cause, failure)
    }

    fun postMessage(message: String) {
        postValue(valueOrDefault.copy(message = message, version = valueOrDefault.version + 1))
    }

    companion object {

        fun <T> LiveData<T>.toStateful(loading: String = "正在加载",
                                       failure: String = "加载失败"): StatefulLiveData<T> {
            val statefulLiveData = StatefulLiveData<T>(loading, failure)
            observeForever(statefulLiveData::postSuccess)
            return statefulLiveData
        }

    }

}