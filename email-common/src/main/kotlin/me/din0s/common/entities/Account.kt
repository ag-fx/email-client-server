package me.din0s.common.entities

data class Account(val username: String, var password: String) {
    val mailbox = Mailbox()
}
