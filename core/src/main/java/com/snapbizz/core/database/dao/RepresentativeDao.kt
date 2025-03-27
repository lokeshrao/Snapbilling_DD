package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.snapbizz.core.database.entities.Representative

@Dao
interface RepresentativeDao : GenericDao<Representative> {

    @Query("SELECT * FROM REPRESENTATIVE")
    suspend fun getAll(): List<Representative>

    @Query("DELETE FROM REPRESENTATIVE")
    fun deleteAll()
}
