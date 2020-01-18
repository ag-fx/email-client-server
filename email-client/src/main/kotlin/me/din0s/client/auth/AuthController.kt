package me.din0s.client.auth

import me.din0s.client.MailClient
import me.din0s.client.auth.events.ClientAuthRQ
import me.din0s.client.auth.events.ClientAuthRS
import me.din0s.common.requests.auth.LoginRQ
import me.din0s.common.requests.auth.RegisterRQ
import tornadofx.Controller

object AuthController : Controller() {
    fun init() {
        subscribe<ClientAuthRQ> {
            val req = when {
                it.isLogin -> LoginRQ(it.user, it.pwd)
                else -> RegisterRQ(it.user, it.pwd)
            }
            with(MailClient.send(req)) {
                println(this)
                fire(ClientAuthRS(this))
            }
        }
    }
}
