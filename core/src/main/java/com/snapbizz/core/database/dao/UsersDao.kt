package com.snapbizz.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.snapbizz.core.database.entities.Users
import java.util.Date


@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: Users)

    @Query("SELECT * FROM USERS WHERE USERNAME = :username")
    suspend fun getUserByUsername(username: String): Users?

    @Query("SELECT * FROM USERS")
    suspend fun getAllUsers(): List<Users>

    @Query("DELETE FROM USERS WHERE USERNAME = :username")
    suspend fun deleteUserByUsername(username: String)

    @Query("UPDATE USERS SET PASSWORD = :password, NAME = :name, ROLE_ID = :roleId, IS_DISABLED = :isDisabled, UPDATED_AT = :updatedAt WHERE USERNAME = :username")
    suspend fun updateUser(
        username: String,
        password: String?,
        name: String?,
        roleId: Int,
        isDisabled: Boolean,
        updatedAt: Date?
    )

    @Query("DELETE FROM USERS")
    fun deleteAll()
}
