@file:JvmName("Util")

package com.numeron.http

import okhttp3.Headers
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.create
import java.io.File
import java.net.URLDecoder


inline fun <reified T> AbstractHttpUtil.create() = retrofit.create<T>()


internal fun getFileNameByUrl(url: String): String {
    //如果Content-Disposition中无法解析出文件名，则取url中的最后一部分字符串作为文件名
    return if (url.contains('\\')) {
        url.substringAfterLast('\\')
    } else {
        url.substringAfterLast('/')
    }.substringBefore('?')
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


fun Headers.getFileSize(): Long {
    return get("Content-Length")?.toLongOrNull() ?: -1
}


fun Headers.getFileType(): String? {
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