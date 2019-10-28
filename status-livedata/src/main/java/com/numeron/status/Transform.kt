@file:JvmName("Transform")

package com.numeron.status

/* 把密封类转换为StatusLayout可识别的枚举类 */
fun <T> Status<out T?>.convert(): com.numeron.view.Status {
    return when (this) {
        is Empty -> com.numeron.view.Status.Empty
        is Failure -> com.numeron.view.Status.Failure
        is Success -> com.numeron.view.Status.Success
        is Loading -> com.numeron.view.Status.Loading
    }
}