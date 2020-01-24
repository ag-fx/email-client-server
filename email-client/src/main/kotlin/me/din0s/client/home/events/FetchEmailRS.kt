package me.din0s.client.home.events

import me.din0s.common.entities.Email
import tornadofx.FXEvent

data class FetchEmailRS(val email: Email) : FXEvent()
