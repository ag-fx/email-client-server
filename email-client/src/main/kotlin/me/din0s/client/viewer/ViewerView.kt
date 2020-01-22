package me.din0s.client.viewer

import javafx.geometry.VPos
import me.din0s.client.writer.events.SendEmailRQ
import me.din0s.common.entities.Email
import tornadofx.*
import java.awt.Color
import java.beans.EventHandler

class ViewerView(private val email: Email) : View(email.subject) {
    init {
        ViewerController.init()
    }

    override fun onDock() {
        super.onDock()

        with(currentStage!!) {
            isResizable = true
            centerOnScreen()
        }
    }

    override val root = anchorpane {
        form {
            fieldset {
                field("From:") {
                    textfield(email.sender) {
                        isEditable = false
                    }
                }
                field("To:") {
                    textfield(email.receiver) {
                        isEditable = false
                    }
                }
                field("Topic:") {
                    textfield(email.subject) {
                        isEditable = false
                    }
                }
                text {
                    requestFocus()
                }
                textarea(email.mainBody) {
                    isEditable = false
                }
            }
        }
    }
}
