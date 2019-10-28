package com.numeron.status

/* 表示数据的状态，[isSuccess]表示成功状态，[value]在成功时表示数据，其它表示*/
sealed class Status<T>(val isSuccess: Boolean, val message: String, val value: T)

/* 表示数据获取成功，用sealed修饰的目的是为了不让其它人使用这个类 */
sealed class Success<T>(message: String, value: T) : Status<T>(true, message, value)

/* 表示数据为空 */
class Empty<T>(message: String) : Success<T?>(message, null)

/* 表示获取数据失败 */
class Failure<T>(message: String, val cause: Throwable?) : Status<T?>(false, message, null)

/* 表示正在加载数据 */
class Loading<T>(message: String, progress: Float) : Status<T?>(false, message, null)

/* Success的实现类，创建后向上转型为Success后提供给外部 */

private class RealSuccess<T>(message: String, value: T) : Success<T>(message, value)

/* impl class & factory functions */

fun <T> successOf(value: T, message: String = "success"): Success<T> = RealSuccess(message, value)

fun <T> emptyOf(message: String) = Empty<T>(message)

fun <T> failureOf(message: String, cause: Throwable? = null) = Failure<T>(message, cause)

fun <T> loadingOf(message: String, progress: Float = 0f) = Loading<T>(message, progress)

/* status functions */

inline fun <T> Status<out T?>.onSuccess(block: (T) -> Unit): Status<out T?> {
    if (isSuccess && this !is Empty) {
        block(value!!)
    }
    return this
}

inline fun <T> Status<out T?>.onFailure(block: (String) -> Unit): Status<out T?> {
    if (this is Failure) {
        block(message)
    }
    return this
}

inline fun <T> Status<out T?>.onLoading(block: (String) -> Unit): Status<out T?> {
    if (this is Loading) {
        block(message)
    }
    return this
}

inline fun <T> Status<out T?>.onEmpty(block: (String) -> Unit): Status<out T?> {
    if (this is Empty) {
        block(message)
    }
    return this
}