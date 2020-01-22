package me.din0s.client.home

import me.din0s.client.MailClient
import me.din0s.client.home.events.*
import me.din0s.common.requests.auth.LogoutRQ
import me.din0s.common.requests.email.DeleteEmailRQ
import me.din0s.common.requests.email.ReadEmailRQ
import me.din0s.common.requests.email.ShowEmailsRQ
import me.din0s.common.responses.email.ReadEmailRS
import me.din0s.common.responses.email.ShowEmailsRS
import me.din0s.common.responses.generic.ErrorRS
import tornadofx.Controller

object HomeController : Controller() {
    fun init() {
        subscribe<FetchListRQ> {
            with(MailClient.send(ShowEmailsRQ)) {
                if (this is ShowEmailsRS) {
                    fire(FetchListRS(this.mailbox))
                } else {
                    error("Unexpected response: $this")
                }
            }
        }

        subscribe<FetchEmailRQ> {
            with(MailClient.send(ReadEmailRQ(it.id))) {
                when (this) {
                    is ReadEmailRS -> {
                        fire(FetchEmailRS(this.email))
                    }
                    is ErrorRS -> {
                        error("Error code: ${this.getCode()}")
                    }
                    else -> {
                        error("Unexpected response: $this")
                    }
                }
            }
        }

        subscribe<PurgeEmailRQ> {
            with(MailClient.send(DeleteEmailRQ(it.id))) {
                fire(PurgeEmailRS(this))
            }
        }

        subscribe<AckEmailRQ> {
            MailClient.send(ReadEmailRQ(it.id, noOp = true))
            fire(AckEmailRS)
        }

        subscribe<DisconnectRQ> {
            MailClient.send(LogoutRQ)
            fire(DisconnectRS)
        }
    }
}
