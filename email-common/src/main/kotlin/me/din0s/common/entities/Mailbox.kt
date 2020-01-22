package me.din0s.common.entities

import java.io.Serializable

class Mailbox: Serializable {
    private val box = arrayListOf<Email>()

    fun getAll(): List<Email> {
        return box.toList()
    }

    fun readEmail(id: String): Email? {
        val (index, email) = box.withIndex().find { (_, e) -> e.id == id } ?: return null
        box[index] = email.copy(isRead = true)
        return email
    }

    fun addEmail(email: Email) {
        box.add(email)
    }

    fun deleteEmail(id: String): Boolean {
        return box.removeIf { it.id == id }
    }
}