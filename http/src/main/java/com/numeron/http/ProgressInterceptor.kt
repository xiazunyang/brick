package com.numeron.http

import okhttp3.*
import okhttp3.internal.closeQuietly
import java.io.File
import java.io.RandomAccessFile
import java.net.URLDecoder

class ProgressInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始请求
        val request = chain.request()
                .newBuilder()
                .addHeader("range", "bytes=0-")
                .addHeader("Connection", "keep-alive")
                .build()
        //取出进度回调
        val progressCallback = request.tag<ProgressCallback>()
        //取出文件
        val fileOrPath = request.tag<File>()
        //获取原始响应
        val response = chain.proceed(request)
        return if (fileOrPath == null && progressCallback == null) {
            //如果没有文件也没有回调，则当普通请求处理
            response
        } else if (fileOrPath != null) {
            //如果有文件参数，则当作下载文件处理
            val responseBody = response.body!!
            val contentLength = responseBody.contentLength()
            val contentType = responseBody.contentType()
            //判断要保存到哪个位置
            val file = if (fileOrPath.extension.isNotEmpty()) {
                fileOrPath
            } else {
                val fileName = response.headers.findFileName() ?: request.url.pathSegments.last()
                File(fileOrPath, fileName)
            }
            //检测、创建存放文件夹
            val parentFile = file.parentFile
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs()
            }
            //获取已保存的文件的大小
            val existLength = file.length()
            val inputSource = when {
                //如果文件存在，直接返回
                existLength == contentLength -> {
                    val fileResponseBody = FileResponseBody(file, contentType)
                    return response.newBuilder().body(fileResponseBody).build()
                }
                //处理文件名重复的错误文件，使用源数据
                existLength > contentLength -> {
                    file.delete()
                    responseBody.source()
                }
                //未获取到文件大小时，使用源数据
                contentLength == -1L -> {
                    //如果文件存在，则删除
                    if (file.exists()) {
                        file.delete()
                    }
                    responseBody.source()
                }
                //重新发起请求，获取其余数据
                else -> {
                    //关闭响应体，释放资源
                    response.closeQuietly()
                    val rangeRequest = request.newBuilder()
                            .removeHeader("range")
                            .addHeader("range", "bytes=${existLength}-").build()
                    chain.proceed(rangeRequest).body!!.source()
                }
            }
            //要写入的文件
            val outputFile = RandomAccessFile(file, "rws")
            outputFile.seek(file.length())
            //把数据写入文件
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var readLength = inputSource.read(buffer)
            while (readLength > 0) {
                outputFile.write(buffer, 0, readLength)
                if (contentLength > 0 && progressCallback != null) {
                    val progress = outputFile.length().toDouble() / contentLength
                    progressCallback.update(progress.toFloat())
                }
                readLength = inputSource.read(buffer)
            }
            outputFile.closeQuietly()
            inputSource.closeQuietly()
            //构建新的响应体并返回
            val fileResponseBody = FileResponseBody(file, contentType)
            return response.newBuilder().body(fileResponseBody).build()
        } else {
            //如果只有进度回调，构建有进度回调的响应，不做IO操作
            val responseBody = ProgressResponseBody(response.body!!, progressCallback)
            return response.newBuilder().body(responseBody).build()
        }
    }

    private fun Headers.findFileName(): String? {
        return get("Content-Disposition")
                ?.split(';')
                ?.let { list ->
                    list.find {
                        it.contains("filename*")
                    } ?: list.find {
                        it.contains("filename")
                    }
                }
                ?.split('=')
                ?.component2()
                ?.removeSurrounding("\"")
                ?.let {
                    if (it.contains('\'')) {
                        //如果有'字符，则取出编码格式与字符串，解码后返回
                        val split = it.split('\'').filter(String::isNotBlank)
                        URLDecoder.decode(split.component2(), split.component1())
                    } else {
                        it  //否则直接返回字符串
                    }
                }
    }

    @Suppress("EXTENSION_SHADOWED_BY_MEMBER")
    private inline fun <reified T> Request.tag(): T? {
        return tag(T::class.java)
    }

}