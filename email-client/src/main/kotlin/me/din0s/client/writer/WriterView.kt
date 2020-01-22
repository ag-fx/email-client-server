package me.din0s.client.writer

import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.geometry.VPos
import me.din0s.client.writer.events.SendEmailRQ
import me.din0s.client.writer.events.SendEmailRS
import me.din0s.common.responses.ResponseCode
import me.din0s.common.responses.generic.ErrorRS
import me.din0s.common.responses.generic.OkRS
import tornadofx.*

class WriterView : View("New Email") {
    private val receiver = SimpleStringProperty()
    private val subject = SimpleStringProperty()
    private val body = SimpleStringProperty()

    init {
        WriterController.init()
    }

    override fun onDock() {
        super.onDock()

        with(currentStage!!) {
            isResizable = false
            centerOnScreen()
        }
    }

    override val root = anchorpane {
        subscribe<SendEmailRS> {
            if (it.res is OkRS) {
                close()
            } else if (it.res is ErrorRS) {
                error("Something went wrong:", it.res.getCode().name)
            }
        }

        form {
            fieldset {
                style {
                    vAlignment = VPos.CENTER
                }

                field("To:") {
                    id = "toField"
                    textfield(receiver)
                }
                field("Subject:") {
                    textfield(subject)
                }
                text()
                textarea(body)
                button("Send!") {
                    style {
                        paddingAll = 12.0
                    }

                    onAction = EventHandler {
                        if (receiver.isEmpty.value) {
                            lookup("#toField").requestFocus()
                        } else {
                            if (subject.isEmpty.value) {
                                subject.value = "No subject"
                            }
                            if (body.isNull.value) {
                                body.value = ""
                            }
                            fire(SendEmailRQ(receiver.value, subject.value, body.value))
                        }
                    }
                }
            }
        }
    }
}
