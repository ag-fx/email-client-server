package me.din0s.common.entities

data class Account(val username: String, var password: String) {
    val mailbox = mutableListOf<Email>()

    fun getEmail(id: Int): Email? {
        return mailbox.find { it.id == id }
    }
}
