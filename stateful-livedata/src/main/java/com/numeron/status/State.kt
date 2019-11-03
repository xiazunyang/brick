package com.numeron.status

/* 表示数据的状态，[isSuccess]表示成功状态，[value]在成功时表示数据，其它表示*/
sealed class State<T>(val value: T)

/* 表示数据获取成功，用sealed修饰的目的是为了不让其它人使用这个类 */
sealed class Success<T>(value: T) : State<T>(value)

/* 表示数据为空 */
class Empty(val message: String) : Success<Nothing?>(null)

/* 表示获取数据失败 */
class Failure(val message: String, val cause: Throwable) : State<Nothing?>(null)

/* 表示正在加载数据 */
class Loading(val message: String, val progress: Float) : State<Nothing?>(null)

/* Success的实现类，创建后向上转型为Success后提供给外部 */

private class RealSuccess<T>(value: T) : Success<T>(value)

/* impl class & factory functions */

fun <T> successOf(value: T): Success<T> = RealSuccess(value)

fun emptyOf(message: String) = Empty(message)

fun failureOf(cause: Throwable, message: String) = Failure(message, cause)

fun loadingOf(message: String, progress: Float) = Loading(message, progress)

/* status functions */

inline fun <T> State<out T?>.onSuccess(block: (T) -> Unit): State<out T?> {
    if (value != null) {
        block(value)
    }
    return this
}

inline fun <T> State<out T?>.onFailure(block: (String, Throwable) -> Unit): State<out T?> {
    if (this is Failure) {
        block(message, cause)
    }
    return this
}

inline fun <T> State<out T?>.onLoading(block: (String, Float) -> Unit): State<out T?> {
    if (this is Loading) {
        block(message, progress)
    }
    return this
}

inline fun <T> State<out T?>.onEmpty(block: (String) -> Unit): State<out T?> {
    if (this is Empty) {
        block(message)
    }
    return this
}