package com.numeron.wandroid.entity

import com.google.gson.annotations.SerializedName
import com.numeron.common.NoArguments

@NoArguments
data class ApiResponse<T>(
        val data: T,
        @SerializedName("errorMsg")
        val errorMessage: String?
)