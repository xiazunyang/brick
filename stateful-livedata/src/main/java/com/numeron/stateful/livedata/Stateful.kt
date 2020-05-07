package com.numeron.stateful.livedata

import com.numeron.common.State

interface Stateful<T> {

    val state: State

    fun onSuccess(c: (T) -> Unit): Stateful<T>

    fun onFailure(c: (String, Throwable) -> Unit): Stateful<T>

    fun onLoading(c: (String, Float) -> Unit): Stateful<T>

    fun onMessage(c: (String) -> Unit): Stateful<T>

}