package com.numeron.wandroid.other

import com.numeron.http.create
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url
import java.io.File

class HttpTest {

    /**
     * 此测试方法验证了OkHttp在下载文件时无法读取进度找问题。
     * 需要在Interceptor中的chain.proceed处下断点
     * 在获取到response后，把本地的网络断开，继续运行后会发现文件依然能成功写入到硬盘中
     * 由此可以证明，网络上所有的重写ResponseBody实现的进度回调，回调的都是从内存写到硬盘的进度，而非真实的下载进度！
     */
    @Test
    fun download() {
        val url = "https://wx3.sinaimg.cn/mw690/007XNw8ily1g8va5ralgoj337k4tc7wr.jpg"
        val path = File("download")
        if (!path.exists()) path.mkdirs()
        val response = runBlocking {
            Http.create<FileDownloadApi>().download(url)
        }
        val outputStream = File(path, "test.jpg").outputStream()
        val responseBody = response.body()!!
        responseBody.byteStream().copyTo(outputStream)
        outputStream.close()
        responseBody.close()
        println("responseBody=$responseBody")
    }

}

interface FileDownloadApi {

    @GET
    @Streaming
    suspend fun download(@Url url: String): Response<ResponseBody>

}
