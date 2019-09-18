package com.numeron.wan.contract

import com.numeron.frame.base.IModel
import com.numeron.frame.mvvm.AbstractViewModel
import com.numeron.frame.mvvm.IView
import com.numeron.wan.entity.JsonResult
import com.numeron.wan.entity.WeChatAuthor
import kotlinx.coroutines.launch
import retrofit2.http.GET


interface MainView : IView<MainViewModel> {

    fun onGotWeChatAuthors(result: Result<List<WeChatAuthor>>)

}


class MainViewModel : AbstractViewModel<MainView, MainModel>() {

    fun getWeChatCreator() {
        launch {
            //通知View显示等待框
            view.showLoading()
            val result = try {
                //切换线程获取网络上的数据
                val list = model.getWeChatAuthor().result
                Result.success(list)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure<List<WeChatAuthor>>(e)
            }
            //通知View关闭等待框并向View层传递结果
            view.hideLoading()
            view.onGotWeChatAuthors(result)
        }
    }

}


class MainModel(mainApi: MainApi) : IModel, MainApi by mainApi


interface MainApi {

    @GET("wxarticle/chapters/json")
    suspend fun getWeChatAuthor(): JsonResult<List<WeChatAuthor>>

}