package com.numeron.wandroid.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.numeron.wandroid.entity.db.Article

@Dao
interface ArticleDao : IDao<Article> {

    @Query("DELETE FROM article WHERE chapterId = :chapterId")
    suspend fun deleteAll(chapterId: Int)

    @Query("SELECT * FROM article WHERE chapterId = :chapterId ORDER BY publishTime DESC")
    fun sourceFactory(chapterId: Int): DataSource.Factory<Int, Article>

}