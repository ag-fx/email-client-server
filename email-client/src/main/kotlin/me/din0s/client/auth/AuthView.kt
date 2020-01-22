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
        MailClient.openConnection()
        with(currentStage!!) {
            isResizable = false
            scene.reset()
            width = MailClient.STARTING_WIDTH
            height = MailClient.STARTING_HEIGHT
            centerOnScreen()
        }
    }

    override val root = vbox {
        alignment = Pos.CENTER

        subscribe<SwitchPage> {
            scene.clear()
            isLogin = !isLogin
            scene.loadLoginText()
        }

        subscribe<ClientAuthRS> {
            if (it.res is OkRS) {
                scene.clear()
                isLogin = true
                scene.loadLoginText()
                replaceWith<HomeView>()
            } else {
                scene.reset()
                if (isLogin) {
                    error("Invalid credentials", "The credentials you provided are invalid. Try again!")
                } else {
                    error("User already exists", "There's already another user with that name!")
                }
            }
        }

        form {
            fieldset("Log In") {
                id = "fieldText"
                alignment = Pos.CENTER
                paddingHorizontal = 12.0

                text()

                field("Username: ") {
                    textfield(username) {
                        id = "name"
                        style {
                            promptTextFill = Color.DARKRED
                        }

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

                field("Password: ") {
                    passwordfield(password) {
                        id = "pwd"
                        style {
                            promptTextFill = Color.DARKRED
                        }

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

                text()

                button("Submit") {
                    id = "submit"

                    action {
                        scene.doSubmit()
                    }

                    onKeyPressed = EventHandler {
                        if (it.code == KeyCode.TAB) {
                            scene.hidePrompts()
                        }
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
                (lookup("#name") as TextField).promptText = "Enter your username"
            }
            if (password.value.isNullOrBlank()) {
                (lookup("#pwd") as TextField).promptText = "Enter your password"
            }
        }
    }

    private fun Scene.loadLoginText() {
        (lookup("#fieldText") as Fieldset).text = when {
            isLogin -> "Log In"
            else -> "Register"
        }
        (lookup("#botLabel") as Label).text = when {
            isLogin -> "Don't have an account? Register now!"
            else -> "Already have an account? Log In!"
        }
    }

    private fun Scene.hidePrompts() {
        with(lookup("#name") as TextField) {
            isDisable = false
            promptText = ""
        }
        with(lookup("#pwd") as TextField) {
            isDisable = false
            promptText = ""
        }
    }

    private fun Scene.reset() {
        hidePrompts()
        lookup("#submit").isDisable = false
        lookup("#botLabel").isDisable = false
        lookup("#progress").style { visibility = FXVisibility.HIDDEN }
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
}
