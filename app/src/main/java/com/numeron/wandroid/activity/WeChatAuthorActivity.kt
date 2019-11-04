package com.numeron.wandroid.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.numeron.adapter.BindingHolder
import com.numeron.adapter.PagedBindingAdapter
import com.numeron.adapter.SpaceItemDecoration
import com.numeron.brick.createViewModel
import com.numeron.util.dp
import com.numeron.wandroid.entity.db.WeChatAuthor
import com.numeron.wandroid.other.*
import com.numeron.view.StatefulMessageObserver
import com.numeron.wandroid.R
import com.numeron.wandroid.contract.WeChatAuthorViewModel
import com.numeron.wandroid.databinding.RecyclerItemWeChatAuthorLayoutBinding
import kotlinx.android.synthetic.main.activity_we_chat_author_layout.*

class WeChatAuthorActivity : AppCompatActivity() {

    private val weChatAuthorViewModel by lazy {
        createViewModel<WeChatAuthorViewModel>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_we_chat_author_layout)

        val adapter = WeChatAuthorAdapter()
        weChatAuthorRecyclerView.adapter = adapter
        weChatAuthorRecyclerView.addItemDecoration(SpaceItemDecoration(4.dp))
        weChatAuthorRefreshLayout.setOnRefreshListener(weChatAuthorViewModel::refresh)
        weChatAuthorRefreshLayout.isEnabled = connectivityManager.isDefaultNetworkActive
        weChatAuthorStatusLayout.setLoadingOperation(weChatAuthorRefreshLayout::setRefreshing)
        weChatAuthorViewModel.weChatAuthorLiveData.observe(this, Observer(adapter::submitList))
        weChatAuthorViewModel.loadStatusLiveData.observe(this, StatefulMessageObserver(weChatAuthorStatusLayout))
    }

    private inner class WeChatAuthorAdapter : PagedBindingAdapter<WeChatAuthor,
            RecyclerItemWeChatAuthorLayoutBinding>(R.layout.recycler_item_we_chat_author_layout) {

        override fun onBindViewHolder(
                holder: BindingHolder<RecyclerItemWeChatAuthorLayoutBinding>, position: Int) {
            val weChatAuthor = getItem(position)
            holder.binding.run {
                if (weChatAuthor == null) {
                    weChatAuthorNameTextView.text = "正在加载，请稍候..."
                } else {
                    setWeChatAuthor(weChatAuthor)
                    executePendingBindings()
                    root.setOnClickListener {
                        startArticleListActivity(weChatAuthor.sid)
                    }
                }
            }
        }
    }

}