package com.snapbizz.core.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.snapbizz.core.database.entities.ProductPacks
import com.snapbizz.core.database.entities.Products

interface GenericDao<out T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAsync(items: List<@UnsafeVariance T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAsync(items: @UnsafeVariance T)

    @RawQuery
    suspend fun getPendingItemsRaw(query: SupportSQLiteQuery): List<T>

    @RawQuery
    suspend fun getPendingItemsCount(query: SupportSQLiteQuery): String

    @RawQuery
    suspend fun markSyncSuccessRaw(query: SupportSQLiteQuery): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateSync(items: List<@UnsafeVariance T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateSync(items: @UnsafeVariance T): Long

    @Update
    suspend fun update(items: @UnsafeVariance T)

    @Delete
    suspend fun delete(items: @UnsafeVariance T)
}