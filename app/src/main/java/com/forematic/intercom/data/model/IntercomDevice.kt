package com.forematic.intercom.data.model

import com.forematic.intercom.data.database.entity.IntercomEntity

data class IntercomDevice(
    val id: Int = 0,
    val programmingPassword: String,
    val signalStrength: Int,
    val adminNumber: String,
    val firstCallOutNumber: String? = null,
    val secondCallOutNumber: String? = null,
    val thirdCallOutNumber: String? = null
)

fun IntercomDevice.toEntity() = IntercomEntity(
    programmingPassword = programmingPassword,
    signalStrength = signalStrength,
    adminNumber = adminNumber,
    firstCallOutNumber = firstCallOutNumber ?: "",
    secondCallOutNumber = secondCallOutNumber ?: "",
    thirdCallOutNumber = thirdCallOutNumber ?: ""
)

fun IntercomEntity.toIntercomDevice() = IntercomDevice(
    id = id,
    programmingPassword = programmingPassword,
    signalStrength = signalStrength,
    adminNumber = adminNumber,
    firstCallOutNumber = firstCallOutNumber.ifBlank { null },
    secondCallOutNumber = secondCallOutNumber.ifBlank { null },
    thirdCallOutNumber = thirdCallOutNumber.ifBlank { null }
)
