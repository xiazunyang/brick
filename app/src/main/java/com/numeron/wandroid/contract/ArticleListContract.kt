package com.numeron.wandroid.contract

import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.numeron.brick.ViewModel
import com.numeron.brick.lazyAutowired
import com.numeron.wandroid.dao.ArticleDao
import com.numeron.wandroid.entity.ApiResponse
import com.numeron.wandroid.entity.Paged
import com.numeron.wandroid.entity.db.Article
import com.numeron.stateful.livedata.StatefulExceptionHandler
import com.numeron.stateful.livedata.StatefulLiveData.Companion.toStateful
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path


interface ArticleListParamProvider {

    val chapterId: Int

}


class ArticleListViewModel(private val paramProvider: ArticleListParamProvider) : ViewModel() {

    private val articleRepository: ArticleRepository by lazyAutowired()

    private val pagedConfig = PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(20)
            .build()

    val articleListLiveData =
            articleRepository.sourceFactory(paramProvider.chapterId)
                    .toLiveData(pagedConfig, boundaryCallback = BoundaryCallback())
                    .toStateful()

    fun refresh() {
        launch {
            articleRepository.deleteAll(paramProvider.chapterId)
        }
    }

    private inner class BoundaryCallback : PagedList.BoundaryCallback<Article>() {

        private var currentPage = 1

        //列表为空时，此方法会在主线程中被调用
        override fun onZeroItemsLoaded() {
            launch(StatefulExceptionHandler(articleListLiveData)) {
                //显示等待动画
                articleListLiveData.postLoading("正在加载文章列表，请稍候...")
                delay(3000)
                val paged = articleRepository.getArticleList(paramProvider.chapterId, 1).data
                currentPage = paged.curPage
                val list = paged.list
                //保存到数据库时，articleListLiveData会自动触发postSuccess
                articleRepository.insert(list)
            }
        }

        //当列表滚动到末尾时，此方法会在主线程中被调用
        override fun onItemAtEndLoaded(itemAtEnd: Article) {
            loadAndSaveArticle(currentPage + 1)
        }

        //当列表滚动到顶部时，此方法会在主线程中被调用
        override fun onItemAtFrontLoaded(itemAtFront: Article) {
            //loadAndSaveArticle(currentPage - 1)
        }

        private fun loadAndSaveArticle(page: Int) {
            //此协程中使用ExceptionHandler来处理异常
            launch(StatefulExceptionHandler(articleListLiveData)) {
                val paged = articleRepository.getArticleList(paramProvider.chapterId, page).data
                currentPage = paged.curPage
                val list = paged.list
                articleRepository.insert(list)
            }
        }

    }

}


class ArticleRepository(dao: ArticleDao, api: ArticleApi) : ArticleDao by dao, ArticleApi by api


interface ArticleApi {

    @GET("wxarticle/list/{chapterId}/{page}/json")
    suspend fun getArticleList(
            @Path("chapterId") chapterId: Int,
            @Path("page") page: Int): ApiResponse<Paged<Article>>

}