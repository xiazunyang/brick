package com.numeron.wandroid.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.numeron.common.Identifiable

@Entity
data class WeChatAuthor(

        @SerializedName("lid")
        @PrimaryKey(autoGenerate = true)
        override val id: Long,

        @SerializedName("id")
        val sid: Int,

        val courseId: Int,

        val name: String,

        val order: Int,

        val parentChapterId: Int,

        val userControlSetTop: Boolean

) : Identifiable<Long>