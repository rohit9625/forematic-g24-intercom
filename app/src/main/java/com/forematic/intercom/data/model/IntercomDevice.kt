package com.forematic.intercom.data.model

import com.forematic.intercom.data.database.entity.IntercomEntity

data class IntercomDevice(
    val id: Int = 0,
    val programmingPassword: String,
    val signalStrength: Int
)

fun IntercomDevice.toEntity() = IntercomEntity(
    programmingPassword = programmingPassword,
    signalStrength = signalStrength

)

fun IntercomEntity.toIntercomDevice() = IntercomDevice(
    id = id,
    programmingPassword = programmingPassword,
    signalStrength = signalStrength
)
