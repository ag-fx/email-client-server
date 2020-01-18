package me.din0s.common.requests.connection

import me.din0s.common.requests.IRequest

interface IConnectionRQ : IRequest {
    override fun needAuth() = false
}
