package com.snapbizz.core.database.dao.global

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.entities.global.Hsn

@Dao
interface HsnDao {
    @Query("SELECT * FROM HSN WHERE HSN_CODE = :hsnCode")
    fun getHsnByCode(hsnCode: String): Hsn?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hsn: Hsn)
}
