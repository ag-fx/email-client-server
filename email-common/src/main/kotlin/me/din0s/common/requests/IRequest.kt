package me.din0s.common.requests

import java.io.Serializable

interface IRequest : Serializable {
    fun needAuth(): Boolean
}
