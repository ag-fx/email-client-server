package me.din0s.client.auth.events

import me.din0s.common.responses.IResponse
import tornadofx.FXEvent

data class ClientAuthRS(val res: IResponse): FXEvent()
