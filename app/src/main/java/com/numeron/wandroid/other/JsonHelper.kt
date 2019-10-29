@file:JvmName("JsonHelper")

package com.numeron.wandroid.other

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.numeron.http.DateDeserializer
import java.text.DateFormat
import java.util.*


val gson: Gson by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
    GsonBuilder()
            .setDateFormat(DateFormat.LONG)
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .create()
}


inline fun <reified T> String.fromJson(): T {
    return gson.fromJson(this, TypeToken.get(T::class.java).type)
}


inline fun <reified T> JsonElement.fromJson(): T {
    return gson.fromJson(this, TypeToken.get(T::class.java).type)
}


inline fun <reified T> T.toJson(): String {
    return gson.toJson(this)
}