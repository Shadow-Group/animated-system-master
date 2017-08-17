package com.osama.project34.imap

import android.os.AsyncTask
import android.util.Log
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

    private inner class RetrieveMessagesTask : AsyncTask<Void, MessagesDataModel, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            for (folder in MailsSharedData.getStore().defaultFolder.list()) {
                if (folder.isOpen) {
                    folder.close(false)
                }
                folder.open(Folder.READ_WRITE)
                Log.d("Osama", "Folder opened")
                if (folder.name.toLowerCase().equals("inbox")) {
                    for (message in folder.messages) {
                        val model = MessagesDataModel(
                                message.receivedDate.toString(),
                                (message.from[0] as InternetAddress).personal,
                                message.messageNumber,
                                message.subject,
                                message.content.toString(),
                                message.allRecipients

                        )
                        publishProgress(model)
                    }
                }
            }
            return false
        }

        override fun onProgressUpdate(vararg values: MessagesDataModel?) {
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