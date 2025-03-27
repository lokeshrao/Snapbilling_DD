package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.Doctors

@Dao
interface DoctorsDao : GenericDao<Doctors> {

    @Query("SELECT * FROM DOCTORS")
    suspend fun getAll(): List<Doctors>

    @Query("DELETE FROM DOCTORS")
    fun deleteAll()
}
