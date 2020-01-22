package me.din0s.common.requests.email

data class ReadEmailRQ(val id: String, val noOp: Boolean = false) : IEmailRQ
