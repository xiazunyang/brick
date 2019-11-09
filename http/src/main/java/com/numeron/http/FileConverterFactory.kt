package com.numeron.http

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.File
import java.lang.reflect.Type

internal class FileConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        if (type == File::class.java) {
            return FileConverter()
        }
        return super.responseBodyConverter(type, annotations, retrofit)
    }

    private class FileConverter : Converter<ResponseBody, File> {

        override fun convert(value: ResponseBody): File? {
            return getFile(value)
        }

        fun getFile(responseBody: ResponseBody): File {
            return if (responseBody is FileResponseBody) {
                responseBody.file
            } else try {
                val field = javaClass.getDeclaredField("delegate")
                field.isAccessible = true
                val delegate = field.get(this) as ResponseBody
                getFile(delegate)
            } catch (throwable: Throwable) {
                throw RuntimeException("响应体中没有记录文件信息！或者没有使用Tag标记File类型的参数！", throwable)
            }
        }

    }


}