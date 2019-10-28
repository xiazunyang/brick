package com.numeron.wan.contract

import androidx.lifecycle.MutableLiveData
import com.numeron.brick.coroutine.AbstractViewModel
import com.numeron.brick.coroutine.loading
import com.numeron.brick.createViewModel
import com.numeron.wan.entity.JsonResult
import com.numeron.wan.entity.WeChatAuthor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.http.GET


interface MainView : IView {

    val mainViewModel: MainViewModel
        get() = createViewModel()

    fun onLoadWeChatAuthorsFailure(throwable: Throwable)

}


class MainViewModel : AbstractViewModel<MainView, MainModel>() {

    val weChatAuthorLiveData = MutableLiveData<List<WeChatAuthor>>()

    fun getWeChatCreator() {
        loading {
            try {
                weChatAuthorLiveData.value = model.getWeChatAuthor().result
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                withContext(Dispatchers.Main) {
                    view.onLoadWeChatAuthorsFailure(throwable)
                }
            }
        }
    }

}


class MainModel(mainApi: MainApi) : MainApi by mainApi


interface MainApi {

    @GET("wxarticle/chapters/jsond")
    suspend fun getWeChatAuthor(): JsonResult<List<WeChatAuthor>>

}