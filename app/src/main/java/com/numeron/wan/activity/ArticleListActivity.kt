package com.numeron.wan.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.numeron.adapter.*
import com.numeron.wan.R
import com.numeron.wan.abs.AbstractMVVMActivity
import com.numeron.wan.contract.ArticleListView
import com.numeron.wan.databinding.ActivityArticleListBinding
import com.numeron.wan.databinding.RecyclerItemArticleListBinding
import com.numeron.wan.entity.Article
import com.numeron.wan.util.EXTRA_AUTHOR_ID
import com.numeron.wan.util.NegativeButton
import com.numeron.wan.util.PositiveButton
import kotlinx.android.synthetic.main.activity_article_list.*

/**
 * 此界面展示了Paging的基本使用方法，并对RxJava的基本使用方法进行了演示。
 */

class ArticleListActivity : AbstractMVVMActivity(), ArticleListView {

    override val authorId: Int
        get() = intent.getIntExtra(EXTRA_AUTHOR_ID, 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val articleListBinding = DataBindingUtil
                .setContentView<ActivityArticleListBinding>(this, R.layout.activity_article_list)
        //绑定viewModel
        articleListBinding.viewModel = articleListViewModel

        //为articleListRecyclerView设置每个Item之间间隔4dp的ItemDecoration
        val dip = (resources.displayMetrics.density * 4).toInt()
        articleListRecyclerView.addItemDecoration(SpaceItemDecoration(dip))
        //绑定Adapter
        val adapter = Adapter()
        articleListRecyclerView.adapter = adapter
        //adapter绑定LiveData
        articleListViewModel.articleListLiveData.observe(this, Observer(adapter::submitList))
        //获取数据
        articleListViewModel.refreshList()
    }

    override fun hideLoading() {
        articleSwipeRefreshLayout.isRefreshing = false
    }

    override fun showLoading(message: String, isCancelable: Boolean) {
        articleSwipeRefreshLayout.isRefreshing = true
    }

    override fun onArticleListLoadError(throwable: Throwable) {
        showDialog(
                "获取文章列表时发生了错误", "提示",
                PositiveButton("重试", articleListViewModel::refreshList),
                NegativeButton("退出", ::finish)
        )
    }

    private inner class Adapter : PagedBindingAdapter<Article, RecyclerItemArticleListBinding>(R.layout.recycler_item_article_list) {

        override fun onBindViewHolder(holder: DataBindingViewHolder<RecyclerItemArticleListBinding>, position: Int) {
            val article = getItem(position) ?: return
            holder.binding.article = article
            holder.binding.executePendingBindings()
            holder.itemView.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.link)))
            }
        }

    }

}