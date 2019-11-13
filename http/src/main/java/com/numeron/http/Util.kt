@file:JvmName("Util")

package com.numeron.http

import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.create
import java.io.File
import java.net.URLDecoder


inline fun <reified T> AbstractHttpUtil.create() = retrofit.create<T>()


fun ResponseBody.toFile(): File {
    return if (this is FileResponseBody) {
        file
    } else try {
        val field = javaClass.getDeclaredField("delegate")
        field.isAccessible = true
        val delegate = field.get(this) as ResponseBody
        delegate.toFile()
    } catch (throwable: Throwable) {
        throw RuntimeException("响应体中没有记录文件信息！或者没有使用Tag标记File类型的参数！", throwable)
    }
}


fun getFileNameByUrl(url: HttpUrl): String {
    return url.pathSegments.last()
}


fun Headers.getFileName(): String? {
    val filenames = get("Content-Disposition")?.split(';')?.filter {
        it.contains("filename")
    }
    if (filenames != null) {
        //优先取编码后的文件名
        val filename = filenames.find {
            it.contains("filename*")
        } ?: filenames.find {
            it.contains("filename")
        }

        val removeSurrounding = filename?.split('=')    //按=字符分割字符串
                ?.component2()  //取第2个
                ?.removeSurrounding("\"")

        if (removeSurrounding != null) {
            return if (removeSurrounding.contains('\'')) {
                //如果有'字符，则取出编码格式与字符串，解码后返回
                val split = removeSurrounding.split('\'').filter(String::isNotBlank)
                URLDecoder.decode(split.component2(), split.component1())
            } else {
                removeSurrounding  //否则直接返回字符串
            }
        }
    }
    return null
}


internal fun Headers.getFileSize(): Long {
    return get("Content-Length")?.toLongOrNull() ?: -1
}


internal fun Headers.getFileType(): String? {
    return get("Content-Type")
}


internal fun getMimeType(extension: String): String? {
    return try {
        val clazz = Class.forName("android.webkit.MimeTypeMap")
        val getSingletonMethod = clazz.getMethod("getSingleton")
        val instance = getSingletonMethod.invoke(null)
        val getMimeTypeMethod = clazz.getMethod("getMimeTypeFromExtension", String::class.java)
        getMimeTypeMethod.invoke(instance, extension) as? String
    } catch (e: Throwable) {
        null
    }
}


@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
internal inline fun <reified T> Request.tag(): T? {
    return tag(T::class.java)
}