package com.numeron.wandroid.contract

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.numeron.brick.createModel
import com.numeron.wandroid.dao.WeChatAuthorDao
import com.numeron.wandroid.entity.ApiResponse
import com.numeron.wandroid.entity.db.WeChatAuthor
import com.numeron.common.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.GET


class WeChatAuthorViewModel : ViewModel() {

    private val weChatAuthorRepository = createModel<WeChatAuthorRepository>()

    val loadStatusLiveData = MutableLiveData<Pair<State, String>>()
    val weChatAuthorLiveData =
            LivePagedListBuilder(weChatAuthorRepository.sourceFactory(), 30)
                    .setBoundaryCallback(BoundaryCallback())
                    .build()

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            weChatAuthorRepository.deleteAll()
        }
    }

    private inner class BoundaryCallback : PagedList.BoundaryCallback<WeChatAuthor>() {
        override fun onZeroItemsLoaded() {
            viewModelScope.launch(Dispatchers.IO) {
                loadStatusLiveData.postValue(State.Loading to "正在加载公众号列表")
                delay(3000)
                try {
                    val weChatAuthorList = weChatAuthorRepository.getWeChatAuthorList().data
                    weChatAuthorRepository.insert(weChatAuthorList)
                    if (weChatAuthorList.isEmpty()) {
                        loadStatusLiveData.postValue(State.Empty to "公众号列表为空")
                    } else {
                        loadStatusLiveData.postValue(State.Success to "公众号列表加载成功")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    loadStatusLiveData.postValue(State.Failure to "加载公众号列表时发生了错误")
                }
            }
        }
    }

}


class WeChatAuthorRepository(dao: WeChatAuthorDao, api: WeChatAuthorsApi) : WeChatAuthorDao by dao, WeChatAuthorsApi by api


interface WeChatAuthorsApi {

    @GET("wxarticle/chapters/json")
    suspend fun getWeChatAuthorList(): ApiResponse<List<WeChatAuthor>>

}