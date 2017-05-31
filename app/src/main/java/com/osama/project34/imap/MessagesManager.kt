package com.osama.project34.imap

import android.os.AsyncTask

/**
 * Created by bullhead on 5/31/17.
 *
 */
class MessagesManager : MailObserver {
    private var callbacks:MailCallbacks?=null

    override fun update(callbacks: MailCallbacks) {
        this.callbacks=callbacks
    }
    private class RetrieveMessagesTask : AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            return false
        }
    }

}