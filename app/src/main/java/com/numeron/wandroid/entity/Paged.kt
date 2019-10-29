package com.numeron.wandroid.entity

import com.google.gson.annotations.SerializedName
import com.numeron.common.NoArguments

@NoArguments
data class Paged<T>(
        val curPage: Int,
        @SerializedName("datas")
        val list: List<T>,
        val offset: Int,
        val over: Boolean,
        val pageCount: Int,
        val size: Int,
        val total: Int
)