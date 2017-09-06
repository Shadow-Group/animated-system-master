package com.osama.project34.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.osama.project34.MailApplication;
import com.osama.project34.data.Folder;
import com.osama.project34.imap.FolderNames;
import com.osama.project34.utils.Constants;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs=getSharedPreferences(Constants.SHARED_PREFS_OAUTH, Context.MODE_PRIVATE);
        //check if prefs has the values set.
        String accountName=prefs.getString(Constants.STRING_ACCOUNT_SHARED_PREFS,null);
        if(accountName!=null){
            String accessToken=prefs.getString(accountName,null);
            if(accessToken!=null){
                startDataActivity(accountName,accessToken);
                return;
            }
        }
        new InitTask().execute();
    }

    private void startDataActivity(String accountName,String accessToken) {
        Intent intent=new Intent(this,DataActivity.class);
        intent.putExtra(Constants.DATA_ACTIVITY_INTENT_PERM,accountName);
        intent.putExtra(Constants.DATA_ACTIVITY_PERM_TOKEN,accessToken);
        startActivityForResult(intent,1);
        finish();
    }

    private void startMainActivity() {
        Intent intent=new Intent(this,MainActivity.class);

        startActivityForResult(intent,1);
        finish();
    }
    private class InitTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            //write folder in database
            Folder folder =new Folder();
            folder.setTitle(FolderNames.INBOX);
            folder.setId(FolderNames.ID_INBOX);
            MailApplication.getDb().insertFolder(folder);
            folder.setTitle(FolderNames.FAVORITE);
            folder.setId(FolderNames.ID_FAVORITE);
            MailApplication.getDb().insertFolder(folder);
            folder.setTitle(FolderNames.SENT);
            folder.setId(FolderNames.ID_SENT);
            MailApplication.getDb().insertFolder(folder);
            folder.setTitle(FolderNames.DRAFT);
            folder.setId(FolderNames.ID_DRAFT);
            MailApplication.getDb().insertFolder(folder);
            folder.setTitle(FolderNames.TRASH);
            folder.setId(FolderNames.ID_TRASH);
            MailApplication.getDb().insertFolder(folder);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            startMainActivity();
        }
    }
}
