package com.numeron.wan.util

import com.numeron.frame.base.IRetrofit
import com.numeron.http.AbstractHttpUtil
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object Http : AbstractHttpUtil(), IRetrofit {

    private val httpLoggingInterceptor = HttpLoggingInterceptor()

    init {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    override val baseUrl: String = "https://wanandroid.com/"

    override fun <T> create(clazz: Class<T>): T = retrofit.create(clazz)

    override val interceptors = listOf(httpLoggingInterceptor)

    override val convertersFactories = listOf(GsonConverterFactory.create())

}