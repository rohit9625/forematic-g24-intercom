package com.forematic.intercom.data.model

import com.forematic.intercom.data.database.entity.IntercomEntity
import com.forematic.intercom.data.database.entity.IntercomWithRelay

data class IntercomDevice(
    val id: Int = 0,
    val programmingPassword: String,
    val signalStrength: Int,
    val adminNumber: String,
    val firstCallOutNumber: String? = null,
    val secondCallOutNumber: String? = null,
    val thirdCallOutNumber: String? = null,
    val micVolume: Int = 0,
    val speakerVolume: Int = 0,
    val timezoneMode: TimezoneMode = TimezoneMode.FREE,
    val firstRelay: Relay? = null,
    val secondRelay: Relay? = null
)

fun IntercomDevice.toEntity() = IntercomEntity(
    programmingPassword = programmingPassword,
    signalStrength = signalStrength,
    adminNumber = adminNumber,
    firstCallOutNumber = firstCallOutNumber ?: "",
    secondCallOutNumber = secondCallOutNumber ?: "",
    thirdCallOutNumber = thirdCallOutNumber ?: "",
    micVolume = micVolume,
    speakerVolume = speakerVolume,
    timezoneMode = timezoneMode.code,
    firstRelay = firstRelay?.id,
    secondRelay = secondRelay?.id
)

fun IntercomEntity.toIntercomDevice() = IntercomDevice(
    id = id,
    programmingPassword = programmingPassword,
    signalStrength = signalStrength,
    adminNumber = adminNumber,
    firstCallOutNumber = firstCallOutNumber.ifBlank { null },
    secondCallOutNumber = secondCallOutNumber.ifBlank { null },
    thirdCallOutNumber = thirdCallOutNumber.ifBlank { null },
    micVolume = micVolume,
    speakerVolume = speakerVolume,
    timezoneMode = TimezoneMode.entries.first { it.code == timezoneMode }
)

fun IntercomWithRelay.toIntercomDevice() = IntercomDevice(
    id = intercom.id,
    programmingPassword = intercom.programmingPassword,
    signalStrength = intercom.signalStrength,
    adminNumber = intercom.adminNumber,
    firstCallOutNumber = intercom.firstCallOutNumber.ifBlank { null },
    secondCallOutNumber = intercom.secondCallOutNumber.ifBlank { null },
    thirdCallOutNumber = intercom.thirdCallOutNumber.ifBlank { null },
    micVolume = intercom.micVolume,
    speakerVolume = intercom.speakerVolume,
    timezoneMode = TimezoneMode.entries.first { it.code == intercom.timezoneMode },
    firstRelay = firstRelay?.toRelay(),
    secondRelay = secondRelay?.toRelay()
)

enum class TimezoneMode(val code: String) {
    FREE("FREE"),
    NIGHT("NIGHT"),
    DAY("DAY"),
    HOLIDAY("HOLS")
}
