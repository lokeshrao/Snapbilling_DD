package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.snapbizz.core.database.entities.Appointments
import com.snapbizz.core.database.entities.AppointmentsWithServices

@Dao
interface AppointmentsDao : GenericDao<Appointments> {

    @Query("SELECT * FROM APPOINTMENTS")
    suspend fun getAll(): List<Appointments>

    @Query("DELETE FROM APPOINTMENTS")
    fun deleteAll()

    @Transaction
    @Query("SELECT * FROM APPOINTMENTS WHERE IS_SYNC_PENDING = 1 LIMIT 100 OFFSET :offset ")
    fun getDataForSync(offset: Int): List<AppointmentsWithServices>
}
