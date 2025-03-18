package com.snapbizz.core.database.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

interface GenericDao<out T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(items: List<@UnsafeVariance T>)

    @RawQuery
    suspend fun getPendingItemsRaw(query: SupportSQLiteQuery): List<T>

    @RawQuery
    suspend fun getPendingItemsCount(query: SupportSQLiteQuery): String

    @RawQuery
    suspend fun markSyncSuccessRaw(query: SupportSQLiteQuery): Int
}