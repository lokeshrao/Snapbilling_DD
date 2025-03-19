package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.snapbizz.core.database.entities.Appointments

@Dao
interface AppointmentsDao : GenericDao<Appointments> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: Appointments)

    @Update
    suspend fun update(appointment: Appointments)

    @Delete
    suspend fun delete(appointment: Appointments)

    @Query("SELECT * FROM APPOINTMENTS")
    suspend fun getAll(): List<Appointments>

    @Query("DELETE FROM APPOINTMENTS")
    fun deleteAll()
}
