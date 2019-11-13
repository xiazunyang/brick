package com.numeron.http

import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Tag
import retrofit2.http.Url
import java.io.File


interface FileDownloadApi {

    /**
     * 下载文件专用的Retrofit Api，可以断点续传。
     * @param url String 要下载的文件的网络地址
     * @param path File 文件要保存到的目录，只能是文件夹
     * @param callback ProgressCallback?    进度回调接口
     * @return Response<ResponseBody> 下载完成的响应体，通过getFile扩展方法来获取下载后的文件
     * @return File 下载完成的文件
     */
    @GET
    @Streaming
    suspend fun download(@Url url: String, @Tag path: File, @Tag callback: ProgressCallback? = null): File

}