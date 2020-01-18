package me.din0s.common.responses.generic

import me.din0s.common.responses.IResponse
import me.din0s.common.responses.ResponseCode

object OkRS : IResponse {
    override fun getCode() = ResponseCode.SUCCESS
}
