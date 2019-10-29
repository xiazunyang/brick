package com.numeron.wandroid.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.numeron.common.Identifiable
import java.util.*


@Entity
data class Article(

        @SerializedName("lid")
        @PrimaryKey(autoGenerate = true)
        override val id: Long,

        @SerializedName("id")
        val sid: Int,

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

        val link: String,

        val niceDate: String,

        val niceShareDate: String,

        val origin: String,

        val prefix: String,

        val projectLink: String,

        val publishTime: Date,

        val selfVisible: Int,

        val shareDate: Date?,

        val shareUser: String,

        val superChapterId: Int,

        val superChapterName: String,

        val title: String,

        val type: Int,

        val userId: Int,

        val visible: Int,

        val zan: Int
) : Identifiable<Long>