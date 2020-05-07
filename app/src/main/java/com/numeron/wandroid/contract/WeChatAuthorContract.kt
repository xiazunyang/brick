package com.numeron.wandroid.contract

import androidx.paging.toLiveData
import com.numeron.brick.ViewModel
import com.numeron.brick.lazyAutowired
import com.numeron.stateful.livedata.StatefulExceptionHandler
import com.numeron.stateful.livedata.StatefulLiveData.Companion.toStateful
import com.numeron.wandroid.dao.WeChatAuthorDao
import com.numeron.wandroid.entity.ApiResponse
import com.numeron.wandroid.entity.db.WeChatAuthor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.GET

class WeChatAuthorViewModel : ViewModel() {

    private val weChatAuthorRepository: WeChatAuthorRepository by lazyAutowired()

    val weChatAuthorLiveData =
            weChatAuthorRepository.sourceFactory().toLiveData(30).toStateful()

    fun refresh() {
        launch {
            if (weChatAuthorLiveData.value.isNullOrEmpty()) {
                getWeChatAuthors()
            } else {
                weChatAuthorRepository.deleteAll()
            }
        }
    }

    fun getWeChatAuthors() {
        launch(StatefulExceptionHandler(weChatAuthorLiveData)) {
            weChatAuthorLiveData.postLoading("正在加载公众号列表")
            delay(3000)
            val weChatAuthorList = weChatAuthorRepository.getWeChatAuthorList().data
            weChatAuthorRepository.insert(weChatAuthorList)
            weChatAuthorLiveData.postMessage("公众号列表加载成功")
        }
    }

}

// 使用Brick创建此类的实例，可自动注入dao层与api层
class WeChatAuthorRepository(dao: WeChatAuthorDao, api: WeChatAuthorsApi) : WeChatAuthorDao by dao, WeChatAuthorsApi by api


interface WeChatAuthorsApi {

    @GET("wxarticle/chapters/json")
    suspend fun getWeChatAuthorList(): ApiResponse<List<WeChatAuthor>>

}