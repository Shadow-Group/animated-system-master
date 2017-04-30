package com.osama.project34.imap;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

/**
 * Created by bullhead on 3/27/17.
 *
 */

public class LabelManager {
    private ArrayList<CharSequence> labels;
    private ImapCallbacks callBacks;
    public LabelManager(Context cts){
        callBacks=(ImapCallbacks)cts;
        labels=new ArrayList<>();
        new LabelTask().execute();
    }

    private class LabelTask extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            Store store=ConnectionManager.getInstance().getmStore();
            if(!store.isConnected()){
                return false;
            }
            try {
                Folder[] folders=store.getPersonalNamespaces();
                for (Folder fol :
                        folders) {
                    labels.add(fol.getName());
                }
            } catch (MessagingException e) {
                e.printStackTrace();
                return false;
            }
            return true;
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
