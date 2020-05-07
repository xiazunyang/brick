package com.numeron.stateful.livedata

import kotlinx.coroutines.CoroutineExceptionHandler
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class StatefulExceptionHandler<T>(private val statefulLiveData: StatefulLiveData<T>) : CoroutineExceptionHandler {

    override val key: CoroutineContext.Key<*>
        get() = CoroutineExceptionHandler

    override fun handleException(context: CoroutineContext, exception: Throwable) {
        exception.printStackTrace()
        when (exception) {
            is StatefulException -> {
                statefulLiveData.postFailure(exception, exception.message)
            }
            is ConnectException, is UnknownHostException -> {
                statefulLiveData.postFailure(exception, "无法连接到服务器，请检查网络后重试。")
            }
            is SocketTimeoutException -> {
                statefulLiveData.postFailure(exception, "网络连接超时，请检查网络后重试。")
            }
            is NullPointerException -> {
                statefulLiveData.postFailure(exception, "没有获取到数据，请稍候重试！")
            }
            else -> {
                statefulLiveData.postFailure(exception)
            }
        }
    }

}