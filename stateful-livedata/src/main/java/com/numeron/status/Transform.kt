@file:JvmName("Transform")

package com.numeron.status

import com.numeron.common.State

/* 把密封类转换为StatusLayout可识别的枚举类 */
fun <T> com.numeron.status.State<out T?>.convert(): State {
    return when (this) {
        is Empty -> State.Empty
        is Failure -> State.Failure
        is Success -> State.Success
        is Loading -> State.Loading
    }
}