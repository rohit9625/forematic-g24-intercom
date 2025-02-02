package com.forematic.intercom.data.database.dao

import androidx.room.Dao
import androidx.room.Upsert
import com.forematic.intercom.data.database.entity.RelayEntity

@Dao
interface RelayDao {
    @Upsert
    suspend fun insertRelay(relay: RelayEntity): Long
}