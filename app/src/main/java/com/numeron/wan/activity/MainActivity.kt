package com.numeron.wan.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.numeron.adapter.BindingAdapter
import com.numeron.adapter.DataBindingViewHolder
import com.numeron.adapter.SpaceItemDecoration
import com.numeron.result.startActivity
import com.numeron.wan.R
import com.numeron.wan.abs.AbstractMVVMActivity
import com.numeron.wan.contract.MainView
import com.numeron.wan.databinding.ActivityMainBinding
import com.numeron.wan.databinding.RecyclerItemWeChatAuthorListBinding
import com.numeron.wan.entity.WeChatAuthor
import com.numeron.wan.util.EXTRA_AUTHOR_ID
import com.numeron.wan.util.NegativeButton
import com.numeron.wan.util.PositiveButton
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AbstractMVVMActivity(), MainView {

    private val listeners = Listeners()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使用DataBinding
        val mainBinding = DataBindingUtil
                .setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mainBinding.viewModel = mainViewModel
        //设置一每个Item之间的间距
        val space = (resources.displayMetrics.density * 4).toInt()
        recyclerView.addItemDecoration(SpaceItemDecoration(space))

        //当LiveData中的数据生发变化时，重新绑定recyclerView的adapter
        mainViewModel.weChatAuthorLiveData.observe(this, Observer {
            recyclerView.adapter = Adapter(it)
        })
        //获取数据
        mainViewModel.getWeChatCreator()
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showLoading(message: String, isCancelable: Boolean) {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun onLoadWeChatAuthorsFailure(throwable: Throwable) {
        showDialog("获取数据时发生了错误，请稍候重试。", "提示",
                PositiveButton("重试", mainViewModel::getWeChatCreator),
                NegativeButton("退出", ::finish)
        )
    }

    private inner class Adapter(
            private val list: List<WeChatAuthor>
    ) : BindingAdapter<RecyclerItemWeChatAuthorListBinding>(list.size, R.layout.recycler_item_we_chat_author_list) {

        override fun onBindViewHolder(holder: DataBindingViewHolder<RecyclerItemWeChatAuthorListBinding>, position: Int) {
            val weChatAuthor = list[position]
            holder.binding.weChatAuthor = weChatAuthor
            holder.binding.listeners = listeners
            holder.binding.executePendingBindings()
        }
    }

    inner class Listeners {

        fun onWeChatAuthorItemClick(weChatAuthor: WeChatAuthor) {
            startActivity<ArticleListActivity>(EXTRA_AUTHOR_ID to weChatAuthor.id)
        }

    }

}