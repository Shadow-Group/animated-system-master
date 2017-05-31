package com.osama.project34.imap

import java.util.*

/**
 * Created by bullhead on 5/31/17.
 *
 */
class MailObjectFactory {
    private var labelManager:LabelManager?=null
    private var observers:ArrayList<MailObserver>?=null
    var callbacks:MailCallbacks?=null
    init {
        labelManager= LabelManager()
        observers= ArrayList()
        observers?.add(labelManager as MailObserver)
    }
    fun notifyObservers(){
        for (observer in observers!!){
            observer.update(this.callbacks!!)
        }
    }

}