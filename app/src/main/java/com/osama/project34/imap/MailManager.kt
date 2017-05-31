package com.osama.project34.imap

/**
 * Created by bullhead on 5/31/17.
 *
 */
class MailManager private constructor() {
    //singleton using companion object method
    companion object{
        private var instance:MailManager?=null
        private var  callbacks: MailCallbacks?=null
        private var objectFactory:MailObjectFactory?=null

        fun getInstance(caller:MailCallbacks):MailManager{
            if(instance==null){
                instance=MailManager()
            }
            callbacks=caller
            objectFactory?.callbacks= callbacks
            objectFactory?.notifyObservers()
            return instance as MailManager
        }
    }
    init{
        objectFactory=MailObjectFactory()
    }
   public fun getObjectFactory():MailObjectFactory {
       return objectFactory as MailObjectFactory
   }

}