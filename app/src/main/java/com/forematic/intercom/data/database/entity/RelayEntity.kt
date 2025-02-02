package com.forematic.intercom.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "relays")
data class RelayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "output_name")
    val outputName: String? = null,

    @ColumnInfo(name = "keypad_code")
    val keypadCode: String? = null,

    @ColumnInfo(name = "keypad_code_location")
    val keypadCodeLocation: String,

    @ColumnInfo(name = "relay_time")
    val relayTime: Int
)
