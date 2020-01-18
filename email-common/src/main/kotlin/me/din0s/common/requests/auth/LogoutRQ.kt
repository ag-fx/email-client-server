package me.din0s.common.requests.auth

object LogoutRQ : IAuthRQ {
    override fun needAuth() = true
}
