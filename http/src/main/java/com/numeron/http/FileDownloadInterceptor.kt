package com.numeron.http

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.RealResponseBody
import okio.buffer
import okio.source
import java.io.File
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL
import kotlin.RuntimeException


internal class FileDownloadInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始请求
        val originRequest = chain.request()
        //获取存放目录
        val path = originRequest.tag<File>()
        //如果这个请求的Tag包含一个文件类型，则判断为下载文件
        if (path != null) {
            //校验File类型
            if (path.isFile) throw RuntimeException("${path}必需是一个文件夹！")
            //获取请求的url
            val url = originRequest.url.toString()
            //构建HEAD请求
            val headRequest = originRequest.newBuilder().head().build()
            //获取响应
            val headResponse = chain.proceed(headRequest)
            //获取响应头
            val headers = headResponse.headers
            //获取文件大小
            val fileSize = headers.getFileSize()
            //获取指定的文件名
            val specifiedFileName = originRequest.tag<String>()
            if (specifiedFileName != null && !specifiedFileName.contains('.')) {
                throw RuntimeException("请指定一个有扩展名的文件！")
            }
            //如果没有指定，则从响应头或url中获取文件名
            val fileName = specifiedFileName ?: headers.getFileName() ?: getFileNameByUrl(url)
            //获取文件类型，从响应头中取或则通过扩展名来推断，如果推断不出来，就当是二进制类型
            val fileType = headers.getFileType()
                    ?: getMimeType(fileName.substringAfterLast('.')) ?: "application/octet-stream"
            //取出进度回调
            val progressCallback = originRequest.tag<ProgressCallback>()
            //检测或创建存放目录
            if (!path.exists()) path.mkdirs()
            //创建文件对象
            val file = File(path, fileName)
            //如果文件体积与要下载的体积相等
            if (file.length() == fileSize) {
                //构建一个完成的响应体并返回
                return generateResponse(file, fileType, fileSize, headResponse)
            } else if (file.length() > fileSize) {
                //如果文件体积大于要下载的体积，则删除本地文件
                file.delete()
            }
            //使用HttpUrlConnection下载文件
            download(url, file, fileSize, progressCallback)
            //构建一个完成的响应体并返回
            return generateResponse(file, fileType, fileSize, headResponse)
        } else {
            return chain.proceed(chain.request())
        }
    }

    private fun generateResponse(file: File, fileType: String, fileSize: Long, response: Response): Response {
        val bufferedSource = file.source().buffer()
        val responseBody = RealResponseBody(fileType, fileSize, bufferedSource)
        return response.newBuilder()
                .body(FileResponseBody(responseBody, file))
                .build()
    }

    private fun download(url: String, file: File, fileSize: Long, progressCallback: ProgressCallback?) {
        val connection = URL(url).openConnection()
        val httpConnection = connection as HttpURLConnection
        httpConnection.addRequestProperty("range", "bytes=${file.length()}-")
        httpConnection.readTimeout = 60000
        httpConnection.connectTimeout = 30000
        httpConnection.connect()
        val inputStream = httpConnection.inputStream
        val randomAccessFile = RandomAccessFile(file, "rws")
        randomAccessFile.seek(file.length())
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var readLength = inputStream.read(buffer)
        while (readLength > -1) {
            randomAccessFile.write(buffer, 0, readLength)
            //仅当进度回调不为空，并且文件大小不小于0时，允许回调
            if (progressCallback != null && fileSize > 0) {
                progressCallback.update(file.length() / fileSize.toFloat())
            }
            readLength = inputStream.read(buffer)
        }
        randomAccessFile.close()
        inputStream.close()
    }

}