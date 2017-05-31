package com.osama.project34.imap;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by home on 1/28/17.
 *
 */

public class ConnectionManager {

    private static final String TAG=ConnectionManager.class.getName();
    private static ConnectionManager instance;
    private MailCallbacks callbacks;

    private Properties       mProps;
    private Session          mSession;
    private Store            mStore;
    private Account          mUserAccount;
    private String           mAccessToken;


    public ConnectionManager(Account acc, String token, Context ctx){
        instance=this;
        mProps=new Properties();
        mProps.put("mail.imap.ssl.enable", "true"); // required for Gmail
        mProps.put("mail.imap.auth.mechanisms", "XOAUTH2");
        this.mUserAccount=acc;
        this.mAccessToken=token;
        this.mSession=Session.getInstance(mProps);
        try{
            callbacks=(MailCallbacks)ctx;
        }catch (Exception ex){
            Log.d(TAG, "ConnectionManager: Necessary for the caller to implement interface MailCallbacks");
            return;
        }
        new ConnectionTask().execute();

    }

    public static ConnectionManager getInstance() throws UnsupportedOperationException{
        if(instance==null){
            throw new UnsupportedOperationException("Instance of this class must be created first" +
                    " from any of the interface class");
        }
        return instance;
    }


    public Store getmStore() {
        return mStore;
    }

    private class ConnectionTask extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
              try {
            mStore=mSession.getStore("imap");
                  return "true";
        } catch ( MessagingException e) {
            Log.d(TAG, "ConnectionManager: Failure in connecting");
            e.printStackTrace();
                  return e.getLocalizedMessage();
        }
        }
        @Override
        protected void onPostExecute(String s) {
            if(s.equals("true")){
                //init check for new messages task
                callbacks.informConnectionStatus(true);

            }else{
                callbacks.connectionError(s);
            }
        }
    }


    public Account getmUserAccount() {
        return mUserAccount;
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

}
