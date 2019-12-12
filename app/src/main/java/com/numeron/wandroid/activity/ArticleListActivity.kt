package com.numeron.wandroid.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import com.numeron.adapter.BindingHolder
import com.numeron.adapter.PagedBindingAdapter
import com.numeron.adapter.SpaceItemDecoration
import com.numeron.brick.lazyViewModel
import com.numeron.delegate.ActivityExtraDelegate
import com.numeron.starter.startActivity
import com.numeron.util.dp
import com.numeron.view.StatefulLayoutMessageObserver
import com.numeron.wandroid.entity.db.Article
import com.numeron.wandroid.other.*
import com.numeron.wandroid.R
import com.numeron.wandroid.contract.ArticleListParamProvider
import com.numeron.wandroid.contract.ArticleListViewModel
import com.numeron.wandroid.databinding.RecyclerItemArticleLayoutBinding
import kotlinx.android.synthetic.main.activity_article_list_layout.*


fun Context.startArticleListActivity(chapterId: Int) {
    startActivity<ArticleListActivity>("chapterId" to chapterId)
}


class ArticleListActivity : BaseActivity(), ArticleListParamProvider {

    override val chapterId: Int by ActivityExtraDelegate(0)

    private val articleListViewModel: ArticleListViewModel by lazyViewModel(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list_layout)
        setSupportActionBar(articleListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        articleListToolbar.setNavigationOnClickListener {
            finish()
        }
        val adapter = ArticleAdapter()
        articleListRecyclerView.adapter = adapter
        articleListRecyclerView.addItemDecoration(SpaceItemDecoration(4.dp))
        //仅在有网络的情况下，允许刷新
        articleListRefreshLayout.setOnRefreshListener(articleListViewModel::refresh)
        articleListRefreshLayout.isEnabled = connectivityManager.isDefaultNetworkActive
        //替换默认的加载动画
        articleListStatusLayout.setLoadingOperation(articleListRefreshLayout::setRefreshing)
        articleListViewModel.articleListLiveData.observe(this, Observer(adapter::submitList))
        articleListViewModel.loadStateLiveData.observe(this, StatefulLayoutMessageObserver(articleListStatusLayout))
    }

    private inner class ArticleAdapter : PagedBindingAdapter<Article,
            RecyclerItemArticleLayoutBinding>(R.layout.recycler_item_article_layout) {

        override fun onBindViewHolder(
                holder: BindingHolder<RecyclerItemArticleLayoutBinding>, position: Int) {
            holder.binding.run {
                val article = getItem(position)
                if (article == null) {
                    articleTitleTextView.text = "正在加载，请稍候..."
                } else {
                    setArticle(article)
                    executePendingBindings()
                    root.setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(article.link)))
                    }
                }
            }
        }
    }

}