package com.numeron.status

import com.numeron.common.State

interface Stateful<T> {

    val state: State

    fun onSuccess(c: (T, String?) -> Unit): Stateful<T>

    fun onFailure(c: (Throwable, String?) -> Unit): Stateful<T>

    fun onLoading(c: (Float, String?) -> Unit): Stateful<T>

    fun onMessage(c: (String) -> Unit): Stateful<T>

    fun onEmpty(c: (String) -> Unit): Stateful<T>

}