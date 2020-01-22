package me.din0s.client.home.events

import me.din0s.common.entities.Email
import tornadofx.EventBus
import tornadofx.FXEvent

class FetchEmailRS(val email: Email) : FXEvent()
