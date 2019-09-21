package com.numeron.wan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import com.numeron.adapter.PagingAdapter
import com.numeron.adapter.SpaceItemDecoration
import com.numeron.adapter.ViewHolder
import com.numeron.wan.R
import com.numeron.wan.abs.AbsMvvmActivity
import com.numeron.wan.contract.ArticleListView
import com.numeron.wan.contract.ArticleListViewModel
import com.numeron.wan.entity.Article
import com.numeron.wan.util.EXTRA_AUTHOR_ID
import com.numeron.wan.util.NegativeButton
import com.numeron.wan.util.PositiveButton
import kotlinx.android.synthetic.main.activity_article_list.*
import kotlinx.android.synthetic.main.item_recycler_article_list_activity.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 此界面展示了Paging的基本使用方法，并对RxJava的基本使用方法进行了演示。
 */

class ArticleListActivity : AbsMvvmActivity<ArticleListViewModel>(), ArticleListView {

    override val authorId: Int
        get() = intent.getIntExtra(EXTRA_AUTHOR_ID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)
        articleSwipeRefreshLayout.setOnRefreshListener(viewModel::refreshList)
        val dip = (resources.displayMetrics.density * 4).toInt()
        articleListRecyclerView.addItemDecoration(SpaceItemDecoration(dip))
        val adapter = Adapter()
        articleListRecyclerView.adapter = adapter
        viewModel.articleListLiveData.observe(this, Observer(adapter::submitList))
        viewModel.refreshList()
    }

    override fun hideLoading() {
        articleSwipeRefreshLayout.isRefreshing = false
    }

    override fun showLoading(message: String?, title: String, isCancelable: Boolean) {
        articleSwipeRefreshLayout.isRefreshing = true
    }

    override fun onArticleListLoadError(throwable: Throwable) {
        showDialog(
                "获取文章列表时发生了错误", "提示",
                PositiveButton("重试", viewModel::refreshList),
                NegativeButton("退出", ::finish)
        )
    }

    private inner class Adapter : PagingAdapter<Article>(R.layout.item_recycler_article_list_activity) {

        private val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA)

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val article = getItem(position) ?: return
            holder.itemView.run {
                articleTitleTextView.text = article.title
                articleAuthorTextView.text = article.author
                articleDateTextView.text = simpleDateFormat.format(article.publishTime)
                setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.link)))
                }
            }
        }
    }

}