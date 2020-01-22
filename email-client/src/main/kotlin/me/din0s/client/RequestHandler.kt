package me.din0s.client

import me.din0s.common.requests.IRequest
import me.din0s.common.responses.IResponse
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class RequestHandler(private val ip: String, private val port: String) {
    private var socket: Socket? = null

    private lateinit var output: ObjectOutputStream
    private lateinit var input: ObjectInputStream

    fun openSocket() {
        if (socket != null) {
            error("A socket is already open!")
        }

        print("> Connecting to ${ip}:$port ... ")
        socket = Socket(ip, port.toInt())
        output = ObjectOutputStream(socket!!.getOutputStream())
        input = ObjectInputStream(socket!!.getInputStream())
        println("Done!")
    }

    fun isOpen(): Boolean {
        return socket != null && !socket!!.isClosed
    }

    fun sendRequest(req: IRequest): IResponse {
        println("> Outgoing request $req")
        output.writeObject(req)
        val res = input.readObject()
        if (res is IResponse) {
            println("> Incoming response $res")
            return res
        } else {
            error("Received unexpected response")
        }
    }

    fun closeSocket() {
        println("> Disconnected from ${ip}:$port")
        if (isOpen()) {
            input.close()
            output.close()
            socket!!.close()
        }
        socket = null
    }
}
