package me.din0s.common.requests.auth

data class RegisterRQ(val username: String, val password: String) : IAuthRQ
