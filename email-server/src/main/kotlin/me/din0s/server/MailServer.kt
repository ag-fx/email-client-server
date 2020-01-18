package me.din0s.server

import me.din0s.common.entities.Account
import me.din0s.common.entities.Email
import me.din0s.common.requests.IRequest
import me.din0s.common.requests.auth.LoginRQ
import me.din0s.common.requests.auth.LogoutRQ
import me.din0s.common.requests.auth.RegisterRQ
import me.din0s.common.requests.connection.ExitRQ
import me.din0s.common.requests.email.DeleteEmailRQ
import me.din0s.common.requests.email.NewEmailRQ
import me.din0s.common.requests.email.ReadEmailRQ
import me.din0s.common.requests.email.ShowEmailsRQ
import me.din0s.common.responses.IResponse
import me.din0s.common.responses.generic.ErrorRS
import me.din0s.common.responses.generic.OkRS
import me.din0s.common.responses.ResponseCode
import me.din0s.common.responses.email.ReadEmailRS
import me.din0s.common.responses.email.ShowEmailsRS
import java.io.EOFException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

fun main(args: Array<String>) {
    when {
        args.isEmpty() -> {
            MailServer.init(port = 4200)
        }
        args.size == 1 -> {
            MailServer.init(port = args[0].toInt())
        }
        else -> {
            error("Unexpected amount of arguments! (expected: 1, received: ${args.size}")
        }
    }
}

object MailServer {
    private val accounts = mutableListOf<Account>()

    fun init(port: Int) {
        ServerSocket(port).use { server ->
            println("> Server running on port $port")
            while (true) {
                val client = server.accept()
                thread {
                    try {
                        ClientHandler(client)
                    } catch (_: EOFException) {}
                }
            }
        }
    }

    fun getUser(user: String): Account? {
        return accounts.find { it.username == user }
    }

    fun authenticateUser(user: String, pwd: String): Account? {
        return accounts.find { it.username == user && it.password == pwd }
    }

    fun addAccount(acc: Account) {
        accounts.add(acc)
    }
}
