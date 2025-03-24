package com.snapbizz.core.database.dao.global
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.entities.global.Vat

@Dao
interface VatDao {
    @Query("SELECT * FROM VAT WHERE _id = :id")
    fun getVatById(id: Int): Vat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vat: Vat)
}
