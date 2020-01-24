package me.din0s.client.home.events

import me.din0s.common.responses.IResponse
import tornadofx.FXEvent

data class PurgeEmailRS(val res: IResponse) : FXEvent()
