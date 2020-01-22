package me.din0s.common.requests.email

data class NewEmailRQ(val receiver: String, val subject: String, val body: String) : IEmailRQ
