package com.forematic.intercom.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.forematic.intercom.data.database.entity.IntercomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IntercomDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntercom(intercom: IntercomEntity)

    @Query("UPDATE intercom SET programming_password = :newPassword WHERE id = 1")
    suspend fun updateProgrammingPassword(newPassword: String)

    @Query("SELECT * FROM intercom WHERE id = 1")
    fun getIntercomDevice(): Flow<IntercomEntity>
}