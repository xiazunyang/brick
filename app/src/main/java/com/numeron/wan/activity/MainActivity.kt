package com.numeron.wan.activity

import android.os.Bundle
import com.numeron.adapter.LiteAdapter
import com.numeron.adapter.SpaceItemDecoration
import com.numeron.adapter.ViewHolder
import com.numeron.result.startActivity
import com.numeron.wan.R
import com.numeron.wan.abs.AbsMvvmActivity
import com.numeron.wan.contract.MainView
import com.numeron.wan.contract.MainViewModel
import com.numeron.wan.entity.WeChatAuthor
import com.numeron.wan.util.EXTRA_AUTHOR_ID
import com.numeron.wan.util.NegativeButton
import com.numeron.wan.util.PositiveButton
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_recycler_we_chat_author_main_activity.view.*


class MainActivity : AbsMvvmActivity<MainViewModel>(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val space = (resources.displayMetrics.density * 4).toInt()
        recyclerView.addItemDecoration(SpaceItemDecoration(space))
        swipeRefreshLayout.setOnRefreshListener(viewModel::getWeChatCreator)
        viewModel.getWeChatCreator()
    }

    override fun hideLoading() {
        super.hideLoading()
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showLoading(message: String?, title: String, isCancelable: Boolean) {
        if (isCancelable) {
            swipeRefreshLayout.isRefreshing = true
        } else {
            super.showLoading(message, title, isCancelable)
        }
    }

    override fun onGotWeChatAuthors(result: Result<List<WeChatAuthor>>) {
        result.onSuccess {
            recyclerView.adapter = Adapter(it)
        }.onFailure {
            showDialog("获取数据时发生了错误，请稍候重试。", "提示",
                    PositiveButton("重试", viewModel::getWeChatCreator),
                    NegativeButton("退出", ::finish)
            )
        }
    }

    private inner class Adapter(
            private val list: List<WeChatAuthor>
    ) : LiteAdapter(list.size, R.layout.item_recycler_we_chat_author_main_activity) {
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val author = list[position]
            holder.itemView.run {
                authorTextView.text = author.name
                setOnClickListener {
                    startActivity<ArticleListActivity>(EXTRA_AUTHOR_ID to author.id)
                }
            }
        }
    }

}