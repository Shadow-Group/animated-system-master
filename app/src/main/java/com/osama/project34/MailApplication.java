package com.osama.project34;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.osama.project34.database.DatabaseHelper;
import com.osama.project34.utils.ConfigManager;



public class MailApplication extends Application {
    private static MailApplication instance;
    private static DatabaseHelper db;

    public static DatabaseHelper getDb() {
        return db;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        instance = this;
        db = DatabaseHelper.getInstance(this);
        ConfigManager.openConfig();
    }

    public static MailApplication getInstance() {
        return instance;
    }
}
