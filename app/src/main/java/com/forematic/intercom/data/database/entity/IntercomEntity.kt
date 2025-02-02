package com.forematic.intercom.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "intercom",
    foreignKeys = [
        ForeignKey(
            entity = RelayEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("first_relay"),
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = RelayEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("second_relay"),
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
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
    val speakerVolume: Int,

    @ColumnInfo(name = "timezone_mode")
    val timezoneMode: String,

    @ColumnInfo(name = "first_relay")
    val firstRelay: Long? = null,

    @ColumnInfo(name = "second_relay")
    val secondRelay: Long? = null
)
