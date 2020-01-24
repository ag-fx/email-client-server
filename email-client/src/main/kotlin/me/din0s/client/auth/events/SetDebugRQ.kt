package me.din0s.client.auth.events

import tornadofx.EventBus
import tornadofx.FXEvent

data class SetDebugRQ(val enabled: Boolean) : FXEvent(EventBus.RunOn.BackgroundThread)
