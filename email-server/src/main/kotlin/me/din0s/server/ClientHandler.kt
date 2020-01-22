package me.din0s.server

import me.din0s.common.entities.Account
import me.din0s.common.entities.Email
import me.din0s.common.entities.Mailbox
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
import me.din0s.common.responses.ResponseCode
import me.din0s.common.responses.email.ReadEmailRS
import me.din0s.common.responses.email.ShowEmailsRS
import me.din0s.common.responses.generic.ErrorRS
import me.din0s.common.responses.generic.OkRS
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class ClientHandler(client: Socket) {
    private val input = ObjectInputStream(client.getInputStream())
    private val output = ObjectOutputStream(client.getOutputStream())

    init {
        var acc: Account? = null
        var run = true
        try {
            while (run) {
                val req = input.readObject()
                if (req !is IRequest) {
                    writeErr(ResponseCode.BAD_REQUEST)
                    error("Received invalid request. Closing connection")
                } else {
                    println("> Incoming request: $req")
                }
                if (req.needAuth() && acc == null) {
                    writeErr(ResponseCode.NO_AUTH)
                } else {
                    when (req) {
                        /* AUTH HANDLING */
                        is RegisterRQ -> {
                            val (user, pwd) = req
                            if (MailServer.getUser(user) != null) {
                                writeErr(ResponseCode.MAIL_EXISTS)
                            } else {
                                acc = Account(user, pwd)
                                MailServer.addAccount(acc)
                                writeOk()
                            }
                        }
                        is LoginRQ -> {
                            val (user, pwd) = req
                            val authAcc = MailServer.authenticateUser(user, pwd)
                            if (authAcc != null) {
                                acc = authAcc
                                writeOk()
                            } else {
                                writeErr(ResponseCode.AUTH_FAIL)
                            }
                        }
                        is LogoutRQ -> {
                            acc = null
                            writeOk()
                        }
                        /* EMAIL HANDLING */
                        is NewEmailRQ -> {
                            val (receiver, subject, body) = req
                            val recipient = MailServer.getUser(receiver)
                            if (recipient == null) {
                                writeErr(ResponseCode.INVALID_RECIPIENT)
                            } else {
                                val sender = acc!!.username
                                val email = Email(sender, receiver, subject, body)
                                acc.mailbox.addEmail(email)
                                writeOk()
                            }
                        }
                        is ShowEmailsRQ -> {
                            write(ShowEmailsRS(acc!!.mailbox.getAll()))
                        }
                        is ReadEmailRQ -> {
                            val (id, pureAck) = req
                            val email = acc!!.mailbox.readEmail(id)
                            if (email == null) {
                                writeErr(ResponseCode.INVALID_EMAIL_ID)
                            } else {
                                if (pureAck) {
                                    writeOk()
                                } else {
                                    write(ReadEmailRS(email))
                                }
                            }
                        }
                        is DeleteEmailRQ -> {
                            val (id) = req
                            if (acc!!.mailbox.deleteEmail(id)) {
                                writeOk()
                            } else {
                                writeErr(ResponseCode.INVALID_EMAIL_ID)
                            }
                        }
                        /* CONNECTION HANDLING */
                        is ExitRQ -> {
                            run = false
                        }
                    }
                }
            }
        } finally {
            output.close()
            input.close()
        }
    }

    private fun write(res: IResponse) {
        println("> Outgoing response: $res")
        output.writeObject(res)
    }

    private fun writeErr(eCode: ResponseCode) {
        write(ErrorRS(eCode))
    }

    private fun writeOk() {
        write(OkRS)
    }
}
