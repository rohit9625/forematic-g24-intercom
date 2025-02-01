package com.forematic.intercom.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intercom")
data class IntercomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "programming_password")
    val programmingPassword: String,
    @ColumnInfo(name = "signal_strength")
    val signalStrength: Int,
    @ColumnInfo(name = "admin_number")
    val adminNumber: String,
    @ColumnInfo(name = "first_call_out_number")
    val firstCallOutNumber: String,
    @ColumnInfo(name = "second_call_out_number")
    val secondCallOutNumber: String,
    @ColumnInfo(name = "third_call_out_number")
    val thirdCallOutNumber: String,
    @ColumnInfo(name = "mic_volume")
    val micVolume: Int,
    @ColumnInfo(name = "speaker_volume")
    val speakerVolume: Int
)
