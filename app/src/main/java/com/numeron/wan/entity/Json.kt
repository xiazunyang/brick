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