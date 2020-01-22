package me.din0s.server

import me.din0s.common.entities.Account
import java.io.EOFException
import java.net.ServerSocket
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
        accounts.add(Account("dinos", "dinos"))
        ServerSocket(port).use { server ->
            println("> Server running on port $port")
            while (true) {
                val client = server.accept()
                println("> Opened connection with ${client.inetAddress.hostAddress}")
                thread {
                    try {
                        ClientHandler(client)
                    } catch (_: EOFException) {}
                    println("> Closed connection with ${client.inetAddress.hostAddress}")
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
