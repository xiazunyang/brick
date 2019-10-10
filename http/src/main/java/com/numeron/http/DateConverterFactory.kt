package com.numeron.http

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateConverterFactory private constructor(private val formatter: (Date) -> String) : Converter.Factory() {

    override fun stringConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<*, String>? {
        if (type == Date::class.java) {
            return DateConverter(formatter)
        }
        return super.stringConverter(type, annotations, retrofit)
    }

    private class DateConverter(private val formatter: (Date) -> String) : Converter<Date, String> {
        override fun convert(value: Date): String? {
            return try {
                formatter(value)
            } catch (e: Exception) {
                null
            }
        }

    }

    companion object {

        @JvmStatic
        @JvmOverloads
        fun create(pattern: String = "yyyy-MM-dd HH:mm:ss", local: Locale = Locale.getDefault()): DateConverterFactory {
            return create(SimpleDateFormat(pattern, local))
        }

        fun create(format: SimpleDateFormat): DateConverterFactory {
            return create(format::format)
        }

        fun create(formatter: (Date) -> String): DateConverterFactory {
            return DateConverterFactory(formatter)
        }

    }

}

