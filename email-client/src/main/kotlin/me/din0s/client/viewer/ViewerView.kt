package me.din0s.client.viewer

import me.din0s.common.entities.Email
import tornadofx.View
import tornadofx.borderpane

class ViewerView(val email: Email) : View("Mail Client") {
    init {
        ViewerController.init()
    }

    override fun onDock() {
        super.onDock()

        with(currentStage!!) {
            isResizable = true
            width = 800.0
            minWidth = 400.0
            minHeight = 300.0
            centerOnScreen()
        }
    }

    override val root = borderpane {

    }
}
