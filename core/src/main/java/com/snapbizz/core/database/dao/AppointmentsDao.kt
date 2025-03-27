package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.Appointments

@Dao
interface AppointmentsDao : GenericDao<Appointments> {

    @Query("SELECT * FROM APPOINTMENTS")
    suspend fun getAll(): List<Appointments>

    @Query("DELETE FROM APPOINTMENTS")
    fun deleteAll()
}
