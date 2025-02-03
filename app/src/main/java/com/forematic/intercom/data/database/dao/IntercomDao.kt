package com.forematic.intercom.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.forematic.intercom.data.database.entity.IntercomEntity
import com.forematic.intercom.data.database.entity.IntercomWithRelay
import kotlinx.coroutines.flow.Flow

@Dao
interface IntercomDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertIntercom(intercom: IntercomEntity)

    @Query("UPDATE intercom SET programming_password = :newPassword WHERE id = 1")
    suspend fun updateProgrammingPassword(newPassword: String)

    @Query("UPDATE intercom SET admin_number = :newNumber WHERE id = 1")
    suspend fun updateAdminNumber(newNumber: String)

    @Query("UPDATE intercom SET first_call_out_number = :newNumber WHERE id = 1")
    suspend fun updateFirstCallOutNumber(newNumber: String)

    @Query("UPDATE intercom SET second_call_out_number = :newNumber WHERE id = 1")
    suspend fun updateSecondCallOutNumber(newNumber: String)

    @Query("UPDATE intercom SET third_call_out_number = :newNumber WHERE id = 1")
    suspend fun updateThirdCallOutNumber(newNumber: String)

    @Query("UPDATE intercom SET mic_volume = :newVolume WHERE id = 1")
    suspend fun updateMicVolume(newVolume: Int)

    @Query("UPDATE intercom SET speaker_volume = :newVolume WHERE id = 1")
    suspend fun updateSpeakerVolume(newVolume: Int)

    @Query("UPDATE intercom SET timezone_mode = :mode WHERE id = 1")
    suspend fun updateTimezoneMode(mode: String)

    @Query("UPDATE intercom SET cli_number = :number WHERE id = 1")
    suspend fun updateCliNumber(number: String)

    @Query("UPDATE intercom SET cli_mode = :mode WHERE id = 1")
    suspend fun updateCliMode(mode: String)

    @Query("UPDATE intercom SET delivery_code = :code WHERE id = 1")
    suspend fun updateDeliveryCode(code: String)

    @Query("SELECT * FROM intercom WHERE id = 1")
    fun getIntercomDevice(): Flow<IntercomEntity>

    @Transaction
    @Query("SELECT * FROM intercom WHERE id = :intercomId")
    fun getIntercomDeviceWithRelays(intercomId: Int): Flow<IntercomWithRelay>
}