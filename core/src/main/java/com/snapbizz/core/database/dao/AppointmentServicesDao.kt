package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.AppointmentServices

@Dao
interface AppointmentServicesDao : GenericDao<AppointmentServices> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointmentService: AppointmentServices)

    @Update
    suspend fun update(appointmentService: AppointmentServices)

    @Delete
    suspend fun delete(appointmentService: AppointmentServices)

    @Query("SELECT * FROM APPOINTMENT_SERVICES")
    suspend fun getAll(): List<AppointmentServices>

    @Query("DELETE FROM APPOINTMENT_SERVICES")
    fun deleteAll()
}
