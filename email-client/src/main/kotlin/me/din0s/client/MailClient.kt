package me.din0s.client

import me.din0s.client.auth.AuthView
import me.din0s.common.requests.IRequest
import me.din0s.common.requests.auth.LogoutRQ
import me.din0s.common.requests.connection.ExitRQ
import me.din0s.common.responses.IResponse
import me.din0s.common.responses.generic.OkRS
import tornadofx.App
import tornadofx.launch
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

fun main(args: Array<String>) {
    val ip: String
    val port: String
    when {
        args.isEmpty() -> {
            ip = "127.0.0.1"
            port = "4200"
        }
        args.size == 2 -> {
            ip = args[0]
            port = args[1]
        }
        else -> {
            error("Unexpected amount of arguments! (expected: 2, received: ${args.size}")
        }
    }
    launch<MailClient>(ip, port)
}

class MailClient : App(AuthView::class) {
    override fun init() {
        super.init()
        val args = parameters.raw
        val ip = args[0]
        val port = args[1]
        print("> Connecting to ${ip}:$port ... ")
        socket = Socket(ip, port.toInt())
        sOut = ObjectOutputStream(socket.getOutputStream())
        sIn = ObjectInputStream(socket.getInputStream())
        println("Done! Launching app.")
    }

    override fun stop() {
        print("> Sending 'EXIT' request ... ")
        sOut.writeObject(ExitRQ)
        socket.close()
        sIn.close()
        sOut.close()
        println("Done, bye!")
        super.stop()
    }

    companion object {
        private lateinit var socket: Socket
        private lateinit var sOut: ObjectOutputStream
        private lateinit var sIn: ObjectInputStream

        fun send(req: IRequest): IResponse {
            println("> Outgoing request $req")
            sOut.writeObject(req)
            val res = sIn.readObject()
            if (res is IResponse) {
                println("> Incoming response $res")
                return res
            } else {
                error("Received unexpected response")
            }
        }
    }
}
