package com.osama.project34.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.osama.project34.MailApplication;
import com.osama.project34.data.Folder;
import com.osama.project34.imap.FolderNames;
import com.osama.project34.utils.Constants;

public class SplashActivity extends AppCompatActivity {
    private static final int RC_PERMISSION=123;
    private boolean hasRequiredPermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //check storage permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RC_PERMISSION);
        }else{
            hasRequiredPermission=true;
        }
        SharedPreferences prefs=getSharedPreferences(Constants.SHARED_PREFS_OAUTH, Context.MODE_PRIVATE);
        //check if prefs has the values set.
        String accountName=prefs.getString(Constants.STRING_ACCOUNT_SHARED_PREFS,null);
        if(accountName!=null){
            String accessToken=prefs.getString(accountName,null);
            if(accessToken!=null && hasRequiredPermission){
                startDataActivity(accountName,accessToken);
                return;
            }
        }
        new InitTask().execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==RC_PERMISSION){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                hasRequiredPermission=true;
            }
        }
    }

    private void startDataActivity(String accountName, String accessToken) {
        Intent intent=new Intent(this,DataActivity.class);
        intent.putExtra(Constants.DATA_ACTIVITY_INTENT_PERM,accountName);
        intent.putExtra(Constants.DATA_ACTIVITY_PERM_TOKEN,accessToken);
        startActivityForResult(intent,1);
        finish();
    }

    private void startMainActivity() {
        if (hasRequiredPermission) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivityForResult(intent, 1);
            finish();
        }else{
            Toast.makeText(this,"I need storage permissions to work.",Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},RC_PERMISSION);
        }
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
