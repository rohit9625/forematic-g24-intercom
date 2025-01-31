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
    val signalStrength: Int
)
