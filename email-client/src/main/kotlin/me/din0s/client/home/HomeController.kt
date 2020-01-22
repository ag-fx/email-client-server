package me.din0s.client.home

import me.din0s.client.MailClient
import me.din0s.client.home.events.*
import me.din0s.common.requests.email.DeleteEmailRQ
import me.din0s.common.requests.email.ReadEmailRQ
import me.din0s.common.requests.email.ShowEmailsRQ
import me.din0s.common.responses.IResponse
import me.din0s.common.responses.email.ReadEmailRS
import me.din0s.common.responses.email.ShowEmailsRS
import me.din0s.common.responses.generic.ErrorRS
import me.din0s.common.responses.generic.OkRS
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
            with(MailClient.send(ReadEmailRQ(it.id, noOp = true))) {
                fire(AckEmailRS(this))
            }
        }
    }
}
