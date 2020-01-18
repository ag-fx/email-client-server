package me.din0s.common.requests.auth

data class LoginRQ(val username: String, val password: String) : IAuthRQ
