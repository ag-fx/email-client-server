package me.din0s.client.writer

import javafx.beans.property.SimpleStringProperty
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import me.din0s.client.writer.events.SendEmailRQ
import me.din0s.client.writer.events.SendEmailRS
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
                error("Your email couldn't be sent", "The server responded with the following error:\n${it.res.getCode().name}")
            }
        }

        form {
            fieldset {
                style {
                    alignment = Pos.CENTER
                }

                field("To:") {
                    textfield(receiver) {
                        id = "recipient"
                        style {
                            promptTextFill = Color.DARKRED
                        }

                        onKeyPressed = EventHandler {
                            promptText = ""
                            if (it.code == KeyCode.ENTER) {
                                scene.lookup("#subject").requestFocus()
                            }
                        }

                        onMouseClicked = EventHandler {
                            promptText = ""
                        }
                    }
                }
                field("Subject:") {
                    textfield(subject) {
                        id = "subject"

                        onKeyPressed = EventHandler {
                            if (it.code == KeyCode.ENTER) {
                                scene.lookup("#body").requestFocus()
                            }
                        }
                    }
                }

                text()

                textarea(body) {
                    id = "body"
                }

                text()

                button("Send!") {
                    onAction = EventHandler {
                        if (receiver.isBlank().value) {
                            (scene.lookup("#recipient") as TextField).promptText = "Please specify a recipient"
                        } else {
                            val rValue = receiver.value
                            val sValue = when {
                                subject.isBlank().value -> "No subject"
                                else -> subject.value
                            }
                            val bValue = when {
                                body.isBlank().value -> "<no body>"
                                else -> body.value
                            }
                            fire(SendEmailRQ(rValue, sValue, bValue))
                        }
                    }

                    onKeyPressed = EventHandler {
                        if (it.code == KeyCode.TAB) {
                            (scene.lookup("#recipient") as TextField).promptText = ""
                        }
                    }
                }
            }
        }
    }
}
