package com.osama.project34.imap

import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.osama.project34.MailApplication
import com.osama.project34.data.Mail
import com.osama.project34.utils.Constants
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

            try {

                val defaultFolder = MailsSharedData.getStore().defaultFolder
                val inbox = defaultFolder.getFolder(FolderNames.ImapNames.INBOX)

                val gmail = defaultFolder.getFolder("[Gmail]")

                val drafts = gmail.getFolder(FolderNames.ImapNames.DRAFTS)
                val sent = gmail.getFolder(FolderNames.ImapNames.SENT)
                val trash = gmail.getFolder(FolderNames.ImapNames.TRASH)

                inbox.open(Folder.READ_WRITE)
                drafts.open(Folder.READ_WRITE)
                sent.open(Folder.READ_WRITE)
                trash.open(Folder.READ_WRITE)


                val fold0: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                fold0.id = FolderNames.ID_DRAFT
                fold0.title = FolderNames.DRAFT
                executor.execute(Threaded(drafts, fold0))


                val fold1: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                fold1.title = FolderNames.TRASH
                fold1.id = FolderNames.ID_TRASH
                executor.execute(Threaded(trash, fold0))

                val fold2: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                fold2.title = FolderNames.SENT
                fold2.id = FolderNames.ID_SENT
                executor.execute(Threaded(sent, fold2))


                val fold3: com.osama.project34.data.Folder = com.osama.project34.data.Folder()
                fold3.id = FolderNames.ID_INBOX
                fold3.title = FolderNames.INBOX
                executor.execute(Threaded(inbox, fold3))

                } catch (ex: MessagingException) {
                Log.e("bullhead", "unable to open folder.")
                    ex.printStackTrace()
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
            var count = 0
            sendMessageNumberBroadCast(folder.messageCount, myFolder.id)
            val id = MailApplication.getDb().getLastMessageId(myFolder)
            for (i in folder.messages.size - 1 downTo 0) {
                if (i > folder.messages.size - id && id != -1) {
                    continue
                }
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
                if(message.allRecipients!=null) {
                    message.allRecipients.mapTo(recipientAddresses) { (it as InternetAddress).address }
                }else{
                    recipientAddresses.add("Draft") //drafts don't have recipient
                }
                model.recipients = recipientAddresses
                MailApplication.getDb().insertMail(model)
                if (count == 5) {
                    Log.d("bullhead", "Sending broadcast for got messages")
                    val intent = Intent(Constants.GOT_MESSAGE_BROADCAST)
                    intent.putExtra(Constants.MESSAGE_FOLDER_ID, myFolder.id)
                    MailApplication.getInstance().sendBroadcast(intent)
                    count = 0
                } else {
                    count++
                }
            }
        }

        private fun sendMessageNumberBroadCast(messages: Int, folderNumber: Int) {
            Log.d("bullhead", "Sending broadcast for messages number: "+folderNumber)
            val intent = Intent(Constants.MESSAGE_NUMBER_BROADCAST)
            intent.putExtra(Constants.MESSAGE_NUMBER_DATA, messages)
            intent.putExtra(Constants.MESSAGE_FOLDER_ID, folderNumber)
            MailApplication.getInstance().sendBroadcast(intent)
        }
    }

}