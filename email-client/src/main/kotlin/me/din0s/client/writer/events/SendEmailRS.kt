package me.din0s.client.writer.events

import me.din0s.common.responses.IResponse
import tornadofx.EventBus
import tornadofx.FXEvent

class SendEmailRS(val res: IResponse) : FXEvent()
