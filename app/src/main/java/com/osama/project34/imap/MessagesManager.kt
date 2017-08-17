package com.osama.project34.imap

import android.os.AsyncTask
import javax.mail.Folder

/**
 * Created by bullhead on 5/31/17.
 *
 */
class MessagesManager : MailObserver {
    private var callbacks:MailCallbacks?=null

    override fun update(callbacks: MailCallbacks) {
        this.callbacks=callbacks
    }
    fun checkMessages():Unit{
        RetrieveMessagesTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
    }
    private class RetrieveMessagesTask : AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            for (folder in MailsSharedData.getAllFolders()){
                if (folder.isOpen){
                    folder.close(false)
                }
                folder.open(Folder.READ_WRITE)
                for (message in folder.messages){
                    println(message.subject)
                }
            }
            return false
        }
    }

}