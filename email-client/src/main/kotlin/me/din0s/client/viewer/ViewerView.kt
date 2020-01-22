package me.din0s.client.viewer

import me.din0s.common.entities.Email
import tornadofx.*

class ViewerView(private val email: Email) : View(email.subject) {
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
                        isFocusTraversable = false
                    }
                }
                field("To:") {
                    textfield(email.receiver) {
                        isEditable = false
                        isFocusTraversable = false
                    }
                }
                field("Topic:") {
                    textfield(email.subject) {
                        isEditable = false
                        isFocusTraversable = false
                    }
                }
                text()
                textarea(email.mainBody) {
                    isEditable = false
                    isFocusTraversable = false
                }
            }
        }
    }
}
