package com.numeron.wandroid.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.numeron.wandroid.entity.db.WeChatAuthor
import com.numeron.wandroid.dao.IDao

@Dao
interface WeChatAuthorDao : IDao<WeChatAuthor> {

    @Query("DELETE FROM WeChatAuthor")
    suspend fun deleteAll()

    @Query("SELECT * FROM WeChatAuthor")
    fun sourceFactory(): DataSource.Factory<Int, WeChatAuthor>

}