package me.din0s.common.responses

import java.io.Serializable

interface IResponse : Serializable {
    fun getCode(): ResponseCode
//    fun getContent(): String? = null
}
