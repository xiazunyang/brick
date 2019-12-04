@file:JvmName("Util")

package com.numeron.http

import retrofit2.create

inline fun <reified T> AbstractHttpUtil.create() = retrofit.create<T>()