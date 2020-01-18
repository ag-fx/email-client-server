package me.din0s.common.requests.email

import me.din0s.common.requests.IRequest

interface IEmailRQ : IRequest {
    override fun needAuth() = true
}
