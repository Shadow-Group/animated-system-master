package com.osama.project34.imap

import android.os.AsyncTask
import android.util.Log
import com.osama.project34.MailApplication
import com.osama.project34.data.Mail
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.mail.Flags
import javax.mail.Folder
import javax.mail.MessagingException
import javax.mail.internet.InternetAddress

/**
 * Created by bullhead on 5/31/17.
 *
 */
class MessagesManager : MailObserver {
    private var callbacks: MailCallbacks? = null

    override fun update(callbacks: MailCallbacks) {
        this.callbacks = callbacks
    }

    fun checkMessages(): Unit {
        RetrieveMessagesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }

    private inner class RetrieveMessagesTask : AsyncTask<Void, Mail, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val executor: ExecutorService = Executors.newCachedThreadPool()
            for (folder in MailsSharedData.getStore().defaultFolder.list()) {
                if (folder.isOpen) {
                    folder.close(false)
                }
                if (folder.list().isNotEmpty()) {
                    for (fol in folder.list()) {
                        if (fol.isOpen) {
                            fol.close(false)
                        }
                        fol.open(Folder.READ_WRITE)
                        when (fol.name) {
                            FolderNames.DRAFT -> {
                                val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                                fold.id = FolderNames.ID_DRAFT
                                fold.title = FolderNames.DRAFT
                                executor.execute(Threaded(fol, fold))
                            }
                            FolderNames.TRASH -> {
                                val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                                fold.title = FolderNames.TRASH
                                fold.id = FolderNames.ID_TRASH
                                executor.execute(Threaded(fol, fold))
                            }
                            FolderNames.SENT, "Sent Mail" -> {
                                val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                                fold.title = FolderNames.SENT
                                fold.id = FolderNames.ID_SENT
                                executor.execute(Threaded(fol, fold))
                            }

                        }
                    }
                }
                try {
                    folder.open(Folder.READ_WRITE)
                    when (folder.name.toLowerCase()) {

                        FolderNames.INBOX -> {
                            val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                            fold.id = FolderNames.ID_INBOX
                            fold.title = FolderNames.INBOX
                            executor.execute(Threaded(folder, fold))
                        }
                        FolderNames.OUTBOX -> {
                            val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                            fold.title = FolderNames.OUTBOX
                            fold.id = FolderNames.ID_OUTBOX
                            executor.execute(Threaded(folder, fold))
                        }

                    }
                } catch (ex: MessagingException) {
                    Log.d("Osama", folder.name)
                    ex.printStackTrace()
                    continue
                }
            }
            return false
        }

        override fun onProgressUpdate(vararg values: Mail?) {
            super.onProgressUpdate(*values)
            if (callbacks != null) {
                callbacks!!.gotTheMessage(values[0])
            } else {
            }
        }


        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!) {
            }
        }
    }

    private class Threaded(val folder: Folder, val myFolder: com.osama.project34.data.Folder) : Runnable {
        override fun run() {
            for (i in folder.messages.size - 1 downTo 0) {
                val message = folder.messages[i]
                val model = Mail()
                model.folderId = myFolder.id
                model.id = message.messageNumber
                model.isFavorite = false
                model.isEncrypted = message.isMimeType(MimeTypes.PGP)
                model.subject = message.subject
                model.date = message.receivedDate.toString()
                model.message = message.content.toString()
                val address: InternetAddress = message.from[0] as InternetAddress
                model.sender = address.personal
                message.flags.userFlags
                        .filter { it.equals(Flags.Flag.SEEN) }
                        .forEach { model.isReadStatus = true }
                val recipientAddresses = ArrayList<String>()
                message.allRecipients.mapTo(recipientAddresses) { (it as InternetAddress).address }
                model.recipients = recipientAddresses
                MailApplication.getDb().insertMail(model)
            }
        }
    }

}