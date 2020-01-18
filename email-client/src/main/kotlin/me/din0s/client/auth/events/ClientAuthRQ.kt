package me.din0s.client.auth.events

import tornadofx.EventBus
import tornadofx.FXEvent

data class ClientAuthRQ(val user: String, val pwd: String, val isLogin: Boolean) : FXEvent(EventBus.RunOn.BackgroundThread)
