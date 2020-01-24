package me.din0s.server

import me.din0s.common.entities.Account
import me.din0s.common.entities.Email
import java.io.EOFException
import java.lang.Exception
import java.net.ServerSocket
import java.net.SocketException
import java.time.OffsetDateTime
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
            error("Unexpected amount of arguments! (expected: 1, received: ${args.size})")
        }
    }
}

object MailServer {
    private val accounts = mutableListOf<Account>()

    private fun setDefaults() {
        val dinosAcc = Account("dinos@auth.gr", "dinos")
        val mail1 = Email("welcome@auth.gr", "dinos@auth.gr", "Welcome", "Hi there!", isRead = true, date = OffsetDateTime.parse("2019-12-03T10:15:30+02:00"))
        val mail2 = Email("support@auth.gr", "dinos@auth.gr", "Need help?", "Hi, dinos.\nIf you need help shoot an email our way!\nRegards.", date = OffsetDateTime.parse("2020-01-05T10:10:10+02:00"))
        val mail3 = Email("junk@mail.gr", "dinos@auth.gr", "YOU WON $1,000,000,000", "Please send me your credit card info...", date = OffsetDateTime.parse("2020-01-20T05:45:01+02:00"))
        with (dinosAcc.mailbox) {
            addEmail(mail1)
            addEmail(mail2)
            addEmail(mail3)
        }
        accounts.add(dinosAcc)

        val adminAcc = Account("admin@auth.gr", "admin")
        val mail4 = Email("no-reply@auth.gr", "admin@auth.gr", "Email created", "Hello world", isRead = true, date = OffsetDateTime.parse("2009-09-01T12:30:00+02:00"))
        val mail5 = Email("support@auth.gr", "admin@auth.gr", "Password reset", "Your new password is [CENSORED]", isRead = true, date = OffsetDateTime.parse("2010-01-01T00:00:00+02:00"))
        val mail6 = Email("admin2@auth.gr", "admin@auth.gr", "Who's this?", "Lorem ipsum...", date = OffsetDateTime.parse("2011-09-04T13:37:42+02:00"))
        with (adminAcc.mailbox) {
            addEmail(mail4)
            addEmail(mail5)
            addEmail(mail6)
        }
        accounts.add(adminAcc)
    }

    fun init(port: Int) {
        setDefaults()
        ServerSocket(port).use { server ->
            println("> Server running on port $port")
            while (true) {
                val client = server.accept()
                println("> Opened connection with ${client.inetAddress.hostAddress}")
                thread {
                    try {
                        ClientHandler(client)
                    } catch (ex: Exception) {
                        when (ex) {
                            is EOFException, is SocketException -> {
                            }
                            else -> throw ex
                        }
                    } finally {
                        println("> Closed connection with ${client.inetAddress.hostAddress}")
                    }
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
