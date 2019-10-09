package com.numeron.wan.abs

import android.os.Bundle
import android.util.Log
import android.view.View
import com.numeron.wan.contract.TestView
import com.numeron.wan.entity.Article
import com.numeron.wan.entity.Paged
import com.numeron.wan.entity.WeChatAuthor


class TestActivity : AbstractMvpActivity(), TestView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(View(this))
        testViewModel.getData()
    }

    override fun onGotArticles(paged: Paged<Article>) {
        Log.e("TestActivity", "paged=$paged")
    }

    override fun onGotWeChatAuthors(authors: List<WeChatAuthor>) {
        Log.e("TestActivity", "authors=$authors")
    }

}