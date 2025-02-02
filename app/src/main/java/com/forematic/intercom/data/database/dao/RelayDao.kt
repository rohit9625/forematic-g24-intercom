package com.forematic.intercom.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.forematic.intercom.data.database.entity.RelayEntity

@Dao
interface RelayDao {
    @Upsert
    suspend fun insertRelay(relay: RelayEntity): Long

    @Query("UPDATE relays SET keypad_code = :keypadCode WHERE id = :relayId")
    suspend fun updateKeypadCode(relayId: Long, keypadCode: String)

    @Query("UPDATE relays SET output_name = :outputName WHERE id = :relayId")
    suspend fun updateOutputName(relayId: Long, outputName: String)

    @Query("UPDATE relays SET relay_time = :relayTime WHERE id = :relayId")
    suspend fun updateRelayTime(relayId: Long, relayTime: Int)
}