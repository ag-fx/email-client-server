package me.din0s.client.writer

import me.din0s.client.MailClient
import me.din0s.client.writer.events.SendEmailRQ
import me.din0s.client.writer.events.SendEmailRS
import me.din0s.common.requests.email.NewEmailRQ
import tornadofx.Controller

object WriterController : Controller() {
    fun init() {
        subscribe<SendEmailRQ> {
            with(MailClient.send(NewEmailRQ(it.receiver, it.subject, it.body))) {
                fire(SendEmailRS(this))
            }
        }
    }
}
