package me.din0s.common.entities

import java.util.concurrent.atomic.AtomicInteger

val counter = AtomicInteger()

data class Email(
    var isRead: Boolean = false,
    val sender: String,
    val receiver: String,
    val subject: String,
    val mainBody: String
) {
    val id = counter.getAndIncrement()
}
