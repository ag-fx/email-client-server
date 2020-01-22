package me.din0s.client.auth

import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
import me.din0s.client.MailClient
import me.din0s.client.auth.events.ClientAuthRQ
import me.din0s.client.auth.events.ClientAuthRS
import me.din0s.client.auth.events.SwitchPage
import me.din0s.client.home.HomeView
import me.din0s.common.responses.generic.OkRS
import tornadofx.*

class AuthView : View("Mail Client") {
    private val username = SimpleStringProperty()
    private val password = SimpleStringProperty()
    private var isLogin = true

    init {
        AuthController.init()
    }

    override fun onDock() {
        super.onDock()
        currentStage?.isResizable = false
    }

    override val root = borderpane {
        subscribe<SwitchPage> {
            isLogin = !isLogin
            scene.reset()
            val name = (lookup("#name") as TextField)
            name.clear()
            name.requestFocus()
            (lookup("#pwd") as TextField).clear()
            (lookup("#fieldText") as Fieldset).text = when {
                isLogin -> "Log In"
                else -> "Register"
            }
            (lookup("#botLabel") as Label).text = when {
                isLogin -> "Don't have an account? Register now!"
                else -> "Already have an account? Log In!"
            }
        }

        subscribe<ClientAuthRS> {
            if (it.res is OkRS) {
                MailClient.user = username.value
                replaceWith<HomeView>()
            } else {
                scene.reset()
                if (isLogin) {
                    error("Invalid credentials", "The credentials you provided are invalid. Try again!")
                } else {
                    warning("User already exists", "There's already another user with that name!")
                }
            }
        }

        center = form {
            fieldset("Log In") {
                id = "fieldText"

                label()

                style {
                    alignment = Pos.CENTER
                    font = Font.font("Roboto")
                }

                field("Username: ") {
                    textfield(username) {
                        id = "name"
                        onMouseClicked = EventHandler {
                            scene.reset()
                        }
                        onKeyPressed = EventHandler {
                            scene.reset()
                            if (it.code == KeyCode.ENTER) {
                                scene.lookup("#pwd").requestFocus()
                            }
                        }
                    }
                }

                label("This field cannot be empty!") {
                    id = "nameError"
                    style {
                        visibility = FXVisibility.HIDDEN
                    }
                }

                text()

                field("Password: ") {
                    passwordfield(password) {
                        id = "pwd"
                        onMouseClicked = EventHandler {
                            scene.reset()
                        }
                        onKeyPressed = EventHandler {
                            scene.reset()
                            if (it.code == KeyCode.ENTER) {
                                val button = scene.lookup("#submit")
                                button.fireEvent(ActionEvent())
                                button.requestFocus()
                            }
                        }
                    }
                }

                label("This field cannot be empty!") {
                    id = "pwdError"
                    style {
                        visibility = FXVisibility.HIDDEN
                    }
                }

                text()

                button("Submit") {
                    id = "submit"
                    action {
                        scene.doSubmit()
                    }

                    style {
                        cursor = Cursor.HAND
                    }
                }

                text()

                progressbar {
                    id = "progress"
                    style {
                        visibility = FXVisibility.HIDDEN
                    }
                }
            }
        }

        bottom = label("Don't have an account? Register now!") {
            id = "botLabel"
            onMouseClicked = EventHandler {
                fire(SwitchPage)
            }
            style {
                cursor = Cursor.HAND
                paddingAll = 2
                underline = true
            }
        }
    }

    private fun Scene.doSubmit() {
        if (!username.value.isNullOrBlank() && !password.value.isNullOrBlank()) {
            fire(ClientAuthRQ(username.value, password.value, isLogin))
            lookup("#name").isDisable = true
            lookup("#pwd").isDisable = true
            lookup("#submit").isDisable = true
            lookup("#botLabel").isDisable = true
            lookup("#progress").style { visibility = FXVisibility.VISIBLE }
        } else {
            if (username.value.isNullOrBlank()) {
                lookup("#nameError").style {
                    visibility = FXVisibility.VISIBLE
                    textFill = Color.DARKRED
                }
            }
            if (password.value.isNullOrBlank()) {
                lookup("#pwdError").style {
                    visibility = FXVisibility.VISIBLE
                    textFill = Color.DARKRED
                }
            }
        }
    }

    private fun Scene.reset() {
        lookup("#name").isDisable = false
        lookup("#pwd").isDisable = false
        lookup("#submit").isDisable = false
        lookup("#botLabel").isDisable = false
        lookup("#progress").style { visibility = FXVisibility.HIDDEN }
        lookup("#nameError").style { visibility = FXVisibility.HIDDEN }
        lookup("#pwdError").style { visibility = FXVisibility.HIDDEN }
    }
}