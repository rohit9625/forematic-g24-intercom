package com.forematic.intercom.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "relays")
data class RelayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "output_name")
    val outputName: String,

    @ColumnInfo(name = "keypad_code")
    val keypadCode: String,

    @ColumnInfo(name = "keypad_code_location")
    val keypadCodeLocation: Int,

    @ColumnInfo(name = "relay_time")
    val relayTime: Int
)
