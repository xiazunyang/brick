package com.numeron.wandroid.contract

import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.numeron.brick.ViewModel
import com.numeron.brick.createModel
import com.numeron.wandroid.dao.ArticleDao
import com.numeron.wandroid.entity.ApiResponse
import com.numeron.wandroid.entity.Paged
import com.numeron.wandroid.entity.db.Article
import com.numeron.common.State
import com.numeron.wandroid.other.IgnoreExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path


interface ArticleListParamProvider {

    val chapterId: Int

}


class ArticleListViewModel(private val paramProvider: ArticleListParamProvider) : ViewModel() {

    private val articleRepository = createModel<ArticleRepository>()

    private val pagedConfig = PagedList.Config.Builder()
            .setPageSize(20)
            .setInitialLoadSizeHint(20)
            .build()

    val loadStateLiveData = MutableLiveData<Pair<State, String>>()

    val articleListLiveData =
            LivePagedListBuilder(articleRepository.sourceFactory(paramProvider.chapterId), pagedConfig)
                    .setBoundaryCallback(BoundaryCallback())
                    .build()

    fun refresh() {
        launch {
            articleRepository.deleteAll(paramProvider.chapterId)
        }
    }

    private inner class BoundaryCallback : PagedList.BoundaryCallback<Article>() {

        private var currentPage = 1

        //列表为空时，此方法会在主线程中被调用
        override fun onZeroItemsLoaded() {
            launch {
                try {
                    //显示等待动画
                    loadStateLiveData.postValue(State.Loading to "正在加载文章列表，请稍候...")
                    delay(3000)
                    val paged = articleRepository.getArticleList(paramProvider.chapterId, 1).data
                    currentPage = paged.curPage
                    val list = paged.list
                    articleRepository.insert(list)
                    if (list.isEmpty()) {
                        loadStateLiveData.postValue(State.Empty to "没有相关文章")
                    } else {
                        loadStateLiveData.postValue(State.Success to "文章列表获取成功")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    loadStateLiveData.postValue(State.Failure to (e.message ?: "加载文章列表时发生了错误"))
                }
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
            launch(IgnoreExceptionHandler()) {
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