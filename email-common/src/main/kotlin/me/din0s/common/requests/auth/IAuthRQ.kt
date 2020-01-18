package me.din0s.common.requests.auth

import me.din0s.common.requests.IRequest

interface IAuthRQ : IRequest {
    override fun needAuth() = false
}
