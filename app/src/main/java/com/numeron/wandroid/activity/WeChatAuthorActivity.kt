package com.numeron.wandroid.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.numeron.adapter.BindingHolder
import com.numeron.adapter.PagedBindingAdapter
import com.numeron.adapter.SpaceItemDecoration
import com.numeron.brick.lazyViewModel
import com.numeron.chameleon.Chameleon
import com.numeron.util.dp
import com.numeron.view.StatefulLayoutMessageObserver
import com.numeron.wandroid.entity.db.WeChatAuthor
import com.numeron.wandroid.other.*
import com.numeron.wandroid.R
import com.numeron.wandroid.contract.WeChatAuthorViewModel
import com.numeron.wandroid.databinding.RecyclerItemWeChatAuthorLayoutBinding
import kotlinx.android.synthetic.main.activity_we_chat_author_layout.*

class WeChatAuthorActivity : BaseActivity() {

    private val weChatAuthorViewModel: WeChatAuthorViewModel by lazyViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_we_chat_author_layout)
        setSupportActionBar(weChatAuthorToolbar)

        val adapter = WeChatAuthorAdapter()
        weChatAuthorRecyclerView.adapter = adapter
        weChatAuthorRecyclerView.addItemDecoration(SpaceItemDecoration(4.dp))
        weChatAuthorRefreshLayout.setOnRefreshListener(weChatAuthorViewModel::refresh)
        weChatAuthorRefreshLayout.isEnabled = connectivityManager.isDefaultNetworkActive
        weChatAuthorStatusLayout.setLoadingOperation(weChatAuthorRefreshLayout::setRefreshing)
        weChatAuthorViewModel.weChatAuthorLiveData.observe(this, Observer(adapter::submitList))
        weChatAuthorViewModel.loadStatusLiveData.observe(this, StatefulLayoutMessageObserver(weChatAuthorStatusLayout))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_we_chat_author, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (menu != null) {
            val isNightMode = Chameleon.getThemeId(this) == R.style.AppTheme_Night
            menu.findItem(R.id.changeTheme)?.isChecked = isNightMode
            return true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.changeTheme -> {
                val themeId = if (item.isChecked) R.style.AppTheme else R.style.AppTheme_Night
                changeTheme(themeId)
                item.isChecked = !item.isChecked
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeTheme(themeId: Int) {
        Chameleon(this, themeId)
                .setBackgroundColor(R.id.weChatAuthorToolbar, R.attr.colorPrimary)
                .setBackgroundColor(R.id.weChatAuthorRefreshLayout, R.attr.colorBackground)
                .setBackgroundColor(R.id.weChatAuthorRootView, R.attr.colorBackgroundLayer1)
                .setTextColor(R.id.weChatAuthorNameTextView, R.attr.colorText)
                .apply(this, R.attr.colorPrimaryDark)
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