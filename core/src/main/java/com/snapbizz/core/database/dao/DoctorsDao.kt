package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.Doctors

@Dao
interface DoctorsDao : GenericDao<Doctors> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(doctor: Doctors)

    @Update
    suspend fun update(doctor: Doctors)

    @Delete
    suspend fun delete(doctor: Doctors)

    @Query("SELECT * FROM DOCTORS")
    suspend fun getAll(): List<Doctors>

    @Query("DELETE FROM DOCTORS")
    fun deleteAll()
}
