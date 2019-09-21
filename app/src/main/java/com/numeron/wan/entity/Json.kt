package com.numeron.wan.entity

import com.google.gson.annotations.SerializedName


data class JsonResult<T>(
        @SerializedName("data")
        val result: T,
        val errorCode: Int,
        val errorMsg: String
)


data class WeChatAuthor(
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int
)


data class Paged<T>(
        val curPage: Int,
        @SerializedName("datas")
        val data: List<T>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)

data class Article(
        val apkLink: String,
        val audit: Int,
        val author: String,
        val chapterId: Int,
        val chapterName: String,
        val collect: Boolean,
        val courseId: Int,
        val desc: String,
        val envelopePic: String,
        val fresh: Boolean,
        val id: Int,
        val link: String,
        val niceDate: String,
        val niceShareDate: String,
        val origin: String,
        val prefix: String,
        val projectLink: String,
        val publishTime: Long,
        val shareDate: Any,
        val shareUser: String,
        val superChapterId: Int,
        val superChapterName: String,
        val title: String,
        val type: Int,
        val userId: Int,
        val visible: Int,
        val zan: Int
)