package me.din0s.client.auth

import javafx.beans.property.SimpleStringProperty
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.geometry.VPos
import javafx.scene.Cursor
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.paint.Color
import javafx.scene.text.Font
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
        with(currentStage!!) {
            isResizable = false
            scene.reset()
            width = 395.0
            height = 375.0
            centerOnScreen()
        }
    }

    override val root = vbox {
        style {
            paddingAll = 15.0
            alignment = Pos.CENTER
        }

        subscribe<SwitchPage> {
            isLogin = !isLogin
            scene.clear()
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
            scene.clear()
            if (it.res is OkRS) {
                replaceWith<HomeView>()
            } else {
                if (isLogin) {
                    error("Invalid credentials", "The credentials you provided are invalid. Try again!")
                } else {
                    warning("User already exists", "There's already another user with that name!")
                }
            }
        }

        form {
            fieldset("Log In") {
                id = "fieldText"

                text()

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
                        fontSize = 14.px
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
                        fontSize = 14.px
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
            }
        }

        progressbar {
            id = "progress"
            prefHeight = 14.0
        }

        label("Don't have an account? Register now!") {
            id = "botLabel"

            style {
                cursor = Cursor.HAND
                underline = true
            }

            onMouseClicked = EventHandler {
                fire(SwitchPage)
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
                    textFill = Color.DARKRED
                    visibility = FXVisibility.VISIBLE
                }
            }
            if (password.value.isNullOrBlank()) {
                lookup("#pwdError").style {
                    textFill = Color.DARKRED
                    visibility = FXVisibility.VISIBLE
                }
            }
        }
    }

    private fun Scene.clear() {
        reset()
        with(lookup("#name") as TextField) {
            clear()
            requestFocus()
        }
        with(lookup("#pwd") as TextField) {
            clear()
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
