package me.din0s.client

import javafx.stage.Stage
import me.din0s.client.auth.AuthView
import me.din0s.common.requests.IRequest
import me.din0s.common.requests.connection.ExitRQ
import me.din0s.common.responses.IResponse
import tornadofx.App
import tornadofx.launch
import java.lang.IllegalStateException

fun main(args: Array<String>) {
    val ip: String
    val port: String
    val debug: String
    when (args.size) {
        0 -> {
            ip = "127.0.0.1"
            port = "4200"
            debug = "false"
        }
        2 -> {
            ip = args[0]
            port = args[1]
            debug = "false"
        }
        3 -> {
            ip = args[0]
            port = args[1]
            debug = args[2]
        }
        else -> {
            System.err.println("Invalid arguments passed!")
            System.err.println("Expected: [ip] [port] (debug)")
            System.err.println("Received: ${args.joinToString(" ")}")
            return
        }
    }
    launch<MailClient>(ip, port, debug)
}

class MailClient : App(AuthView::class) {
    override fun init() {
        super.init()
        val args = parameters.raw
        val ip = args[0]
        val port = args[1]
        val debug = args[2]
        handler = RequestHandler(ip, port, debug.equals("true", true))
    }

    override fun start(stage: Stage) {
        stage.width = STARTING_WIDTH
        stage.height = STARTING_HEIGHT - 4.0
        super.start(stage)
    }

    override fun stop() {
        if (handler.isOpen()) {
            send(ExitRQ)
        }
        closeConnection()
        try {
            super.stop()
        } catch(_: IllegalStateException) {}
    }

    companion object {
        const val STARTING_WIDTH = 400.0
        const val STARTING_HEIGHT = 275.0

        private lateinit var handler: RequestHandler

        fun openConnection() {
            handler.openSocket()
        }

        fun debug(enable: Boolean) {
            handler.setDebug(enable)
        }

        fun send(req: IRequest): IResponse {
            return handler.sendRequest(req)
        }

        fun closeConnection() {
            handler.closeSocket()
        }
    }
}
