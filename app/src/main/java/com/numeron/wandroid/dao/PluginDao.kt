package com.numeron.wandroid.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.numeron.wandroid.entity.db.Plugin
import com.numeron.wandroid.dao.IDao


@Dao
interface PluginDao: IDao<Plugin> {

    @Query("DELETE FROM plugin WHERE userId = :userId")
    suspend fun deleteAll(userId: Long)

    @Query("SELECT * FROM plugin WHERE userId = :userId")
    fun find(userId: Long): DataSource.Factory<Int, Plugin>

}