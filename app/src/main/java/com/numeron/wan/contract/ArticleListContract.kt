package com.numeron.wan.contract

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.numeon.brick.AbstractViewModel
import com.numeon.brick.IModel
import com.numeon.brick.IView
import com.numeon.brick.createViewModel
import com.numeron.rx.LoadingResultObserver
import com.numeron.rx.ResultObserver
import com.numeron.wan.entity.Article
import com.numeron.wan.entity.JsonResult
import com.numeron.wan.entity.Paged
import com.numeron.wan.util.Http
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit


interface ArticleListView : IView {

    val articleListViewModel: ArticleListViewModel
        get() = createViewModel(Http)

    val authorId: Int
    fun onArticleListLoadError(throwable: Throwable)

}


class ArticleListViewModel : AbstractViewModel<ArticleListView, ArticleListModel>() {

    val articleListLiveData = MutableLiveData<PagedList<Article>>()

    fun refreshList() {
        RxPagedListBuilder(ArticleListFactory(), 10)
                .buildObservable()
                .subscribe(articleListLiveData::setValue)
    }

    private inner class ArticleListFactory : DataSource.Factory<Int, Article>() {
        override fun create(): DataSource<Int, Article> = ArticleListDataSource()
    }

    private inner class ArticleListDataSource : PageKeyedDataSource<Int, Article>() {

        override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Article>) {
            //paging加载第一页数据时会调用此方法，此方法在后台线程中运行
            model.getArticlePagedListByAuthor(1, view.authorId)
                    //在加载数据时，让线程暂停5秒，如果在此之前退出Activity，也不会发生内存泄漏
                    .delay(5, TimeUnit.SECONDS)
                    //从JsonResult中取出result
                    .map(JsonResult<Paged<Article>>::result)
                    //将Observer的处理线程指定为主线程
                    //因为要显示等待框、并且加载失败时要将错误传回View层，都需要在主线程才能处理
                    .observeOn(AndroidSchedulers.mainThread())
                    //LoadingResultObserver会在订阅时显示等待框并在结束时关闭
                    .subscribe(LoadingResultObserver(view, "正在加载文章列表") { result ->
                        result.onFailure(view::onArticleListLoadError)
                                .onSuccess { paged ->
                                    callback.onResult(paged.data, 0, paged.total, null, 2)
                                }
                    })
        }

        override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
            model.getArticlePagedListByAuthor(params.key, view.authorId)
                    .map(JsonResult<Paged<Article>>::result)
                    .subscribe(ResultObserver(view) {
                        it.onSuccess { paged ->
                            val adjacentPageKey = if (paged.curPage < paged.pageCount) params.key + 1 else null
                            callback.onResult(paged.data, adjacentPageKey)
                        }
                    })

        }

        override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
            model.getArticlePagedListByAuthor(params.key, view.authorId)
                    .map(JsonResult<Paged<Article>>::result)
                    .subscribe(ResultObserver(view) {
                        it.onSuccess { paged ->
                            val adjacentPageKey = if (paged.curPage > 1) params.key - 1 else null
                            callback.onResult(paged.data, adjacentPageKey)
                        }
                    })
        }

    }

}


class ArticleListModel(api: ArticleListApi) : IModel, ArticleListApi by api


interface ArticleListApi {

    /**
     * 获取指定公众号中的文章列表，当[keyword]}不为空时，则只获取与[keyword]匹配的文章。
     * @param page Int  第几页，从1开始
     * @param authorId Int  哪个公众号
     * @param keyword String?   搜索关键字
     * @return Observable<JsonResult<List<Article>>>
     */
    @GET("wxarticle/list/{author}/{page}/json")
    fun getArticlePagedListByAuthor(
            @Path("page") page: Int,
            @Path("author") authorId: Int,
            @Query("k") keyword: String? = null): Observable<JsonResult<Paged<Article>>>

}