package com.numeron.wan.contract

import com.numeon.brick.*
import com.numeon.brick.coroutine.AbstractViewModel
import com.numeron.wan.entity.Article
import com.numeron.wan.entity.JsonResult
import com.numeron.wan.entity.Paged
import com.numeron.wan.entity.WeChatAuthor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


interface TestView : IView {

    val testViewModel: TestViewModel
        get() = createViewModel(TestViewModel::class.java)

    fun onGotArticles(paged: Paged<Article>)

    fun onGotWeChatAuthors(authors: List<WeChatAuthor>)

}

class TestViewModel : AbstractViewModel<TestView, TestModel>() {

    fun getData() {
        /*launch {
               runCatching(model.getWeChatAuthor()::result)
                       .onSuccess(view::onGotWeChatAuthors)
        }*/

        model.getArticlePagedListByAuthor(1, 408)
                .map(JsonResult<Paged<Article>>::result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::onGotArticles, Throwable::printStackTrace)
    }

}


class TestModel(articleListApi: ArticleListApi, mainApi: MainApi) : IModel, ArticleListApi by articleListApi, MainApi by mainApi