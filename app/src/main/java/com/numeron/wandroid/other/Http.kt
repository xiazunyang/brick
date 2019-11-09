package com.numeron.wandroid.other

import com.numeron.http.AbstractHttpUtil
import com.numeron.http.DateConverterFactory
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory


object Http : AbstractHttpUtil() {

    override val baseUrl: String = "https://wanandroid.com/"

    override val header: Map<String, String>
        get() = mapOf("Accept-Language" to "zh-cn,zh;q=0.8")

    override val convertersFactories: Iterable<Converter.Factory> = listOf(
            DateConverterFactory.create("yyyy-MM-dd'T'HH:mm:ss"),
            GsonConverterFactory.create(gson)
    )

}