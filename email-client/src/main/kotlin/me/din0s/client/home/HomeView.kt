package me.din0s.client.home

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.ContextMenu
import javafx.scene.control.MenuItem
import javafx.scene.control.TableColumn
import javafx.scene.control.TableRow
import javafx.scene.text.FontWeight
import javafx.util.Callback
import me.din0s.client.MailClient
import me.din0s.client.auth.AuthView
import me.din0s.client.home.events.*
import me.din0s.client.viewer.ViewerView
import me.din0s.client.writer.WriterView
import tornadofx.*
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class HomeView : View("Mail Client") {
    init {
        HomeController.init()
        fire(FetchListRQ)
    }

    override fun onDock() {
        super.onDock()
        with(currentStage!!) {
            isResizable = true
            minWidth = 550.0
            minHeight = 300.0
            width = 800.0
            height = 600.0
            centerOnScreen()
        }
    }

    private class EmailPreview(id: String, subject: String, sender: String, read: Boolean, date: OffsetDateTime) {
        val idProperty = SimpleStringProperty(id)
        val id by idProperty

        val subjectProperty = SimpleStringProperty(subject)
        val subject by subjectProperty

        val senderProperty = SimpleStringProperty(sender)
        val sender by senderProperty

        val readProperty = SimpleBooleanProperty(read)
        var read by readProperty

        val dateProperty = SimpleStringProperty(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(date).replace("T", " "))
        var date by dateProperty
    }

    override val root = borderpane {
        val emails = observableListOf<EmailPreview>()

        subscribe<FetchListRS> {
            emails.setAll(it.mailbox.sortedByDescending { e -> e.date }.map { e -> EmailPreview(e.id, e.subject, e.sender, e.isRead, e.date) })
        }

        subscribe<FetchEmailRS> {
            ViewerView(it.email).openWindow()
        }

        left = borderpane() {
            paddingAll = 12.0

            top = vbox {
                button("New Email") {
                    prefWidth = 150.0

                    onAction = EventHandler {
                        WriterView().openWindow()
                    }
                }

                label()

                button("Fetch Emails") {
                    prefWidth = 150.0

                    onAction = EventHandler {
                        fire(FetchListRQ)
                    }
                }
            }

            bottom = button("Log Out") {
                onAction = EventHandler {
                    fire(DisconnectRQ)
                    replaceWith<AuthView>()
                }
            }
        }

        center = tableview(emails) {
            id = "table"
            placeholder = label("Your emails will show up here :)")

            readonlyColumn("ID", EmailPreview::id).cellFormat {
                text = item.toString()
                style {
                    alignment = Pos.CENTER_RIGHT
                }
            }
            readonlyColumn("Status", EmailPreview::read).cellFormat {
                text = when {
                    !item -> "Unread"
                    else -> "Read"
                }
                style {
                    alignment = Pos.CENTER
                    fontWeight = when {
                        !item -> FontWeight.BOLD
                        else -> FontWeight.NORMAL
                    }
                }
            }
            readonlyColumn("Sender", EmailPreview::sender)
            readonlyColumn("Subject", EmailPreview::subject)
            val sortCol = readonlyColumn("Date", EmailPreview::date) {
                sortType = TableColumn.SortType.DESCENDING
            }

            sortOrder.add(sortCol)

            rowFactory = Callback {
                fun markAsRead(email: EmailPreview) {
                    email.read = true
                    fire(AckEmailRQ(email.id))
                    emails.replace(email)
                }

                fun openEmail(email: EmailPreview) {
                    email.read = true
                    fire(FetchEmailRQ(email.id))
                    emails.replace(email)
                }

                fun deleteEmail(email: EmailPreview) {
                    fire(PurgeEmailRQ(email.id))
                    emails.remove(email)
                }

                val row = TableRow<EmailPreview>()
                val menu = ContextMenu()

                val openItem = MenuItem("Open Email")
                val markItem = MenuItem("Mark as Read")
                val delItem = MenuItem("Delete Email")
                openItem.onAction = EventHandler {
                    openEmail(row.item)
                }
                markItem.onAction = EventHandler {
                    markAsRead(row.item)
                }
                delItem.onAction = EventHandler {
                    deleteEmail(row.item)
                }

                menu.items.add(openItem)
                menu.items.add(markItem)
                menu.items.add(delItem)
                row.contextMenuProperty().bind(Bindings.`when`(!row.emptyProperty()).then(menu).otherwise(null as ContextMenu?))

                row.onDoubleClick {
                    if (!row.isEmpty) {
                        openEmail(row.item)
                    }
                }

                row
            }
        }
    }

    private fun <T : Any> ObservableList<T>.replace(element: T) {
        val index = indexOf(element)
        set(index, element)
    }
}
