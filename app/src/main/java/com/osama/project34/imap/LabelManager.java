package com.osama.project34.imap;

import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.event.MessageChangedEvent;
import javax.mail.event.MessageChangedListener;

/**
 * Created by bullhead on 3/27/17.
 *
 */

public class LabelManager implements MailObserver {
    private ArrayList<CharSequence> labels;
    private MailCallbacks callBacks;

    public void startGettingLabels(){
        labels=new ArrayList<>();
        new LabelTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void update(@NotNull MailCallbacks callbacks) {
        this.callBacks=callbacks;
    }

    private class LabelTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            Store store= MailsSharedData.getStore();
            try {
                if(!store.isConnected()){
                    Log.d("label", "doInBackground: store is not connected");
                    store.connect();
                }
                addAllLabels(store.getDefaultFolder());
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
        private void addAllLabels(Folder folder) throws MessagingException{
            if(folder.list().length>0){
                for (Folder f:folder.list()) {
                    addAllLabels(f);
                }
            }else{
                MailsSharedData.addFolder(folder);
                labels.add(folder.getName());
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean){
                callBacks.updateLabels(labels);
            }else{
                callBacks.connectionError("Error getting labels");
            }
        }
    }



}
