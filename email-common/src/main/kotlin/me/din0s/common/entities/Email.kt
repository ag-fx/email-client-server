package me.din0s.common.entities

import me.nimavat.shortid.ShortId
import java.io.Serializable
import java.time.OffsetDateTime

data class Email(
    val sender: String,
    val receiver: String,
    val subject: String,
    val mainBody: String,
    var isRead: Boolean = false,
    val id: String = ShortId.generate(),
    val date: OffsetDateTime = OffsetDateTime.now()
) : Serializable
