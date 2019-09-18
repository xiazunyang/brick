package com.numeron.http

import okhttp3.Interceptor
import okhttp3.Response


class AddHeaderInterceptor(private val headers: Map<String, String>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
                .newBuilder()
                .also { builder ->
                    headers.forEach {
                        builder.addHeader(it.key, it.value)
                    }
                }
                .build()
                .let(chain::proceed)
    }

}