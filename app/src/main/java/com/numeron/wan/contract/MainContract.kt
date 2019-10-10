package com.numeron.wan.contract

import androidx.lifecycle.MutableLiveData
import com.numeon.brick.IModel
import com.numeon.brick.IView
import com.numeon.brick.coroutine.AbstractViewModel
import com.numeon.brick.createViewModel
import com.numeron.wan.entity.JsonResult
import com.numeron.wan.entity.WeChatAuthor
import kotlinx.coroutines.launch
import retrofit2.http.GET


interface MainView : IView {

    val mainViewModel: MainViewModel
        get() = createViewModel()

    fun onLoadWeChatAuthorsFailure(throwable: Throwable)

}


class MainViewModel : AbstractViewModel<MainView, MainModel>() {

    val weChatAuthorLiveData = MutableLiveData<List<WeChatAuthor>>()

    fun getWeChatCreator() {
        launch {
            //通知View显示等待框
            view.showLoading()
            try {
                //切换线程获取网络上的数据，并将结果放入weChatAuthorLiveData中
                weChatAuthorLiveData.value = model.getWeChatAuthor().result
            } catch (e: Exception) {
                e.printStackTrace()
                view.onLoadWeChatAuthorsFailure(e)
            }
            //通知View关闭等待框并向View层传递结果
            view.hideLoading()
        }
    }

}


class MainModel(mainApi: MainApi) : IModel, MainApi by mainApi


interface MainApi {

    @GET("wxarticle/chapters/json")
    suspend fun getWeChatAuthor(): JsonResult<List<WeChatAuthor>>

}