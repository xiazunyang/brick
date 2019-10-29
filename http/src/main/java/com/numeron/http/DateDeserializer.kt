package com.numeron.http

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        val value = json.asJsonPrimitive
        return when {
            value.isNumber ->
                Date(value.asLong)
            value.isString ->
                SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(value.asString)
            else -> throw IllegalStateException()
        }
    }

}