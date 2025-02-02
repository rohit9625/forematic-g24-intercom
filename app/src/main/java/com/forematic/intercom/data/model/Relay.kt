package com.forematic.intercom.data.model

import com.forematic.intercom.data.database.entity.RelayEntity

data class Relay(
    val id: Long = 0,
    val outputName: String? = null,
    val keypadCode: String? = null,
    val keypadCodeLocation: String,
    val relayTime: Int = 5 // Default relay time is 5 seconds
)

fun Relay.toEntity() = RelayEntity(
    outputName = outputName,
    keypadCode = keypadCode,
    keypadCodeLocation = keypadCodeLocation,
    relayTime = relayTime
)

fun RelayEntity.toRelay() = Relay(
    id = id,
    outputName = outputName,
    keypadCode = keypadCode,
    keypadCodeLocation = keypadCodeLocation,
    relayTime = relayTime
)