package me.din0s.client.writer.events

import tornadofx.EventBus
import tornadofx.FXEvent

data class SendEmailRQ(val receiver: String, val subject: String, val body: String) : FXEvent(EventBus.RunOn.BackgroundThread)
