package com.osama.project34.imap

import android.os.AsyncTask
import android.util.Log
import com.osama.project34.data.Mail
import com.sun.mail.util.MimeUtil
import org.jetbrains.kotlin.utils.addToStdlib.cast
import javax.activation.MimeType
import javax.mail.Flags
import javax.mail.Folder
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
            for (folder in MailsSharedData.getStore().defaultFolder.list()) {
                if (folder.isOpen) {
                    folder.close(false)
                }
                folder.open(Folder.READ_WRITE)
                when (folder.name) {
                    FolderNames.DRAFT -> {
                        val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                        fold.id = FolderNames.ID_DRAFT
                        fold.title = FolderNames.DRAFT
                        for (message in folder.messages) {
                            val model = Mail()
                            model.folderId=fold.id
                            model.id=message.messageNumber
                            model.isFavorite=false
                            model.isEncrypted=message.isMimeType(MimeTypes.PGP)
                            model.subject=message.subject
                            model.message=message.content.toString()
                            val address:InternetAddress= message.from[0] as InternetAddress
                            model.sender=address.address
                            model.date=message.receivedDate.toString()
                            message.flags.userFlags
                                    .filter { it.equals(Flags.Flag.SEEN) }
                                    .forEach { model.isReadStatus=true }

                            publishProgress(model)
                        }
                    }
                    FolderNames.INBOX -> {
                        val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                        fold.id = FolderNames.ID_INBOX
                        fold.title = FolderNames.INBOX
                        for (message in folder.messages) {
                            val model = Mail()
                            model.folderId=fold.id
                            model.id=message.messageNumber
                            model.isFavorite=false
                            model.isEncrypted=message.isMimeType(MimeTypes.PGP)
                            model.subject=message.subject
                            model.date=message.receivedDate.toString()
                            model.message=message.content.toString()
                            val address:InternetAddress= message.from[0] as InternetAddress
                            model.sender=address.address
                            message.flags.userFlags
                                    .filter { it.equals(Flags.Flag.SEEN) }
                                    .forEach { model.isReadStatus=true }

                            publishProgress(model)
                        }
                    }
                    FolderNames.OUTBOX -> {
                        val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                        fold.title = FolderNames.OUTBOX
                        fold.id = FolderNames.ID_OUTBOX
                        for (message in folder.messages) {
                            val model = Mail()
                            model.folderId=fold.id
                            model.id=message.messageNumber
                            model.isFavorite=false
                            model.isEncrypted=message.isMimeType(MimeTypes.PGP)
                            model.subject=message.subject
                            model.date=message.receivedDate.toString()
                            model.message=message.content.toString()
                            val address:InternetAddress= message.from[0] as InternetAddress
                            model.sender=address.address
                            message.flags.userFlags
                                    .filter { it.equals(Flags.Flag.SEEN) }
                                    .forEach { model.isReadStatus=true }

                            publishProgress(model)
                        }
                    }
                    FolderNames.TRASH -> {
                        val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                        fold.title = FolderNames.TRASH
                        fold.id = FolderNames.ID_TRASH
                        for (message in folder.messages) {
                            val model = Mail()
                            model.folderId=fold.id
                            model.id=message.messageNumber
                            model.isFavorite=false
                            model.date=message.receivedDate.toString()
                            model.isEncrypted=message.isMimeType(MimeTypes.PGP)
                            model.subject=message.subject
                            model.message=message.content.toString()
                            val address:InternetAddress= message.from[0] as InternetAddress
                            model.sender=address.address
                            message.flags.userFlags
                                    .filter { it.equals(Flags.Flag.SEEN) }
                                    .forEach { model.isReadStatus=true }

                            publishProgress(model)
                        }
                    }
                    FolderNames.SENT -> {
                        val fold: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                        fold.title = FolderNames.SENT
                        fold.id = FolderNames.ID_SENT
                        for (message in folder.messages) {
                            val model = Mail()
                            model.folderId=fold.id
                            model.id=message.messageNumber
                            model.isFavorite=false
                            model.isEncrypted=message.isMimeType(MimeTypes.PGP)
                            model.subject=message.subject
                            model.date=message.receivedDate.toString()
                            model.message=message.content.toString()
                            val address:InternetAddress= message.from[0] as InternetAddress
                            model.sender=address.address
                            message.flags.userFlags
                                    .filter { it.equals(Flags.Flag.SEEN) }
                                    .forEach { model.isReadStatus=true }

                            publishProgress(model)
                        }
                    }
                }
            }
            return false
        }

        override fun onProgressUpdate(vararg values: Mail?) {
            super.onProgressUpdate(*values)
            if (callbacks != null) {
                Log.d("Osama", "Folder opened")
                callbacks!!.gotTheMessage(values[0])
            } else {
                Log.d("Osama", "Null null null")
            }
        }


        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!) {
            }
        }
    }

}