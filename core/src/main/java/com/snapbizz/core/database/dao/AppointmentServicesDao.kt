package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.AppointmentServices

@Dao
interface AppointmentServicesDao : GenericDao<AppointmentServices> {

    @Query("SELECT * FROM APPOINTMENT_SERVICES")
    suspend fun getAll(): List<AppointmentServices>

    @Query("DELETE FROM APPOINTMENT_SERVICES")
    fun deleteAll()
}
