package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.Representative

@Dao
interface RepresentativeDao : GenericDao<Representative> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(representative: Representative)

    @Update
    suspend fun update(representative: Representative)

    @Delete
    suspend fun delete(representative: Representative)

    @Query("SELECT * FROM REPRESENTATIVE")
    suspend fun getAll(): List<Representative>

    @Query("DELETE FROM REPRESENTATIVE")
    fun deleteAll()
}
