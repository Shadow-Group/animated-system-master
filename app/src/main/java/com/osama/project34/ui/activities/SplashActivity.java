package com.osama.project34.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        startMainActivity();
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
}
