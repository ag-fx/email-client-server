package me.din0s.common.responses.email

import me.din0s.common.entities.Email

data class ShowEmailsRS(val mailbox: List<Email>) : IEmailRS
