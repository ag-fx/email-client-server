package me.din0s.common.responses.generic

import me.din0s.common.responses.IResponse
import me.din0s.common.responses.ResponseCode

data class ErrorRS(private val code: ResponseCode) : IResponse {
    override fun getCode() = code
}
