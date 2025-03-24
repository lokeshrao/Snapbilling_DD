package com.snapbizz.core.database.dao.global
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.entities.global.Gst

@Dao
interface GstDao {
    @Query("SELECT * FROM GST WHERE GST_ID = :gstId")
    fun getGstById(gstId: Int): Gst?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gst: Gst)
}
