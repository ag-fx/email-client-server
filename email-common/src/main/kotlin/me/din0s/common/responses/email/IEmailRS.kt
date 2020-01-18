package me.din0s.common.responses.email

import me.din0s.common.responses.IResponse
import me.din0s.common.responses.ResponseCode

interface IEmailRS : IResponse {
    override fun getCode() = ResponseCode.SUCCESS
}
