package me.din0s.client.home

import me.din0s.client.MailClient
import me.din0s.client.home.events.*
import me.din0s.common.requests.auth.LogoutRQ
import me.din0s.common.requests.email.DeleteEmailRQ
import me.din0s.common.requests.email.ReadEmailRQ
import me.din0s.common.requests.email.ShowEmailsRQ
import me.din0s.common.responses.email.ReadEmailRS
import me.din0s.common.responses.email.ShowEmailsRS
import tornadofx.Controller

object HomeController : Controller() {
    fun init() {
        subscribe<FetchListRQ> {
            with(MailClient.send(ShowEmailsRQ)) { this as ShowEmailsRS
                fire(FetchListRS(this.mailbox))
            }
        }

        subscribe<FetchEmailRQ> {
            with(MailClient.send(ReadEmailRQ(it.id))) {
                if (this is ReadEmailRS) {
                    fire(FetchEmailRS(this.email))
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
