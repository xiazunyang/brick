@file:JvmName("Transform")

package com.numeron.status

/* 把密封类转换为StatusLayout可识别的枚举类 */
fun <T> State<out T?>.convert(): com.numeron.common.State {
    return when (this) {
        is Empty -> com.numeron.common.State.Empty
        is Failure -> com.numeron.common.State.Failure
        is Success -> com.numeron.common.State.Success
        is Loading -> com.numeron.common.State.Loading
    }
}