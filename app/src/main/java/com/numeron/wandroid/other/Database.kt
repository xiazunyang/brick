package com.numeron.wandroid.other

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.numeron.wandroid.dao.ArticleDao
import com.numeron.wandroid.dao.WeChatAuthorDao
import com.numeron.wandroid.entity.db.Article
import com.numeron.wandroid.entity.db.WeChatAuthor


@Database(
        entities = [
            Article::class,
            WeChatAuthor::class
        ], version = 1, exportSchema = false
)
@TypeConverters(
        value = [
            DatabaseTypeConverter::class
        ]
)
abstract class Database : RoomDatabase() {

    abstract val articleDao: ArticleDao
    abstract val weChatAuthorDao: WeChatAuthorDao

}