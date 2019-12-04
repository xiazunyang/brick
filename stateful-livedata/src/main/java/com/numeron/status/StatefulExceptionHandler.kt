package com.numeron.status

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlin.coroutines.CoroutineContext

class StatefulExceptionHandler<T>(private val statefulLiveData: StatefulLiveData<T>) : CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        exception.printStackTrace()
        statefulLiveData.postFailure(exception)
    }

}