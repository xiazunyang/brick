package com.numeron.http

import okhttp3.Interceptor
import okhttp3.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.net.HttpURLConnection
import java.net.URL


internal class FileDownloadInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        //获取原始请求
        val originRequest = chain.request()
        //获取存放目录
        val fileOrPath = originRequest.tag<File>()
        //如果这个请求的Tag包含一个文件类型，则判断为下载文件
        if (fileOrPath != null) {
            //获取请求的url
            val httpUrl = originRequest.url
            val url = httpUrl.toString()
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
            //取出进度回调
            val progressCallback = originRequest.tag<ProgressCallback>()
            //判断提供的File是文件还是文件夹，如果没有指定文件名字并且File有扩展名，则判断为文件
            val file = if (specifiedFileName == null && fileOrPath.extension.isNotEmpty()) fileOrPath else {
                //否则在File文件夹下创建一个新文件
                val fileName = specifiedFileName
                        ?: headers.getFileName() ?: getFileNameByUrl(httpUrl)
                File(fileOrPath, fileName)
            }
            //获取文件类型，从响应头中取类型，如果没有，则通过扩展名来推断
            val fileType = headers.getFileType() ?: getMimeType(file.extension)
            //检测或创建存放目录
            val parentFile: File? = file.parentFile
            if (parentFile != null) {
                if (parentFile.exists()) {
                    if (parentFile.isFile) throw FileNotFoundException("无法创建${file}的存放目录！")
                } else {
                    parentFile.mkdirs()
                }
            }
            //如果文件体积与要下载的体积相等
            if (file.length() == fileSize) {
                //构建一个完成的响应体并返回
                return generateResponse(file, fileType, headResponse)
            } else if (file.length() > fileSize) {
                //如果文件体积大于要下载的体积，则删除本地文件
                file.delete()
            }
            //使用HttpUrlConnection下载文件
            download(url, file, fileSize.toFloat(), progressCallback)
            //构建一个完成的响应体并返回
            return generateResponse(file, fileType, headResponse)
        } else {
            return chain.proceed(chain.request())
        }
    }

    private fun generateResponse(file: File, fileType: String?, response: Response): Response {
        return response.newBuilder().body(FileResponseBody(file, fileType)).build()
    }

    private fun download(url: String, file: File, fileSize: Float, progressCallback: ProgressCallback?) {
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
                progressCallback.update(file.length() / fileSize)
            }
            readLength = inputStream.read(buffer)
        }
        httpConnection.disconnect()
        randomAccessFile.close()
        inputStream.close()
    }

}