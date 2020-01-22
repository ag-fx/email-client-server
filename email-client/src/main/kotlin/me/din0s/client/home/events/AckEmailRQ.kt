package me.din0s.client.home.events

import tornadofx.EventBus
import tornadofx.FXEvent

data class AckEmailRQ(val id: String) : FXEvent(EventBus.RunOn.BackgroundThread)
