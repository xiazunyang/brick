package com.numeron.wandroid.other

import com.numeron.status.StatefulLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext


class IgnoreExceptionHandler : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) = exception.printStackTrace()
}


class StatefulExceptionHandler<T>(private val statefulLiveData: StatefulLiveData<T>) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {
    override fun handleException(context: CoroutineContext, exception: Throwable) {
        statefulLiveData.postFailure(exception)
    }
}