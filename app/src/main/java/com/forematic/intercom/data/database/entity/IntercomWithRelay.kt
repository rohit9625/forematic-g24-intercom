package com.forematic.intercom.data.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class IntercomWithRelay(
    @Embedded val intercom: IntercomEntity,

    @Relation(parentColumn = "first_relay", entityColumn = "id")
    val firstRelay: RelayEntity?,

    @Relation(parentColumn = "second_relay", entityColumn = "id")
    val secondRelay: RelayEntity?
)