@file:JvmName("Transform")

package com.numeron.status

/* 把密封类转换为StatusLayout可识别的枚举类 */
fun <T> Status<out T?>.convert(): com.numeron.common.Status {
    return when (this) {
        is Empty -> com.numeron.common.Status.Empty
        is Failure -> com.numeron.common.Status.Failure
        is Success -> com.numeron.common.Status.Success
        is Loading -> com.numeron.common.Status.Loading
    }
}