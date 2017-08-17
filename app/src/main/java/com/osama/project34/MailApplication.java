package com.osama.project34;

import android.app.Application;

import com.osama.project34.utils.ConfigManager;

/**
 * Created by home on 3/4/17.
 *
 */

public class MailApplication extends Application {
    private static MailApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
            instance=this;
            ConfigManager.openConfig();
    }

    public static MailApplication getInstance(){
        return instance;
    }
}
