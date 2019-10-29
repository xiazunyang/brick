package com.numeron.wandroid.dao

import androidx.room.*
import com.numeron.common.Identifiable

@Dao
interface IDao<T : Identifiable<Long>> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg entities: T): LongArray

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entities: Collection<T>): List<Long>

    @Delete
    suspend fun delete(entity: T)

    @Delete
    suspend fun delete(vararg entities: T)

    @Delete
    suspend fun delete(entities: Collection<T>)

    @Update
    suspend fun update(entity: T): Int

    @Update
    suspend fun update(vararg entities: T): Int

    @Update
    suspend fun update(entities: Collection<T>): Int

}