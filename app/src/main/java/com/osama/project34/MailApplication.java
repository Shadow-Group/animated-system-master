package com.osama.project34;

import android.app.Application;

import com.osama.project34.utils.ConfigManager;

/**
 * Created by home on 3/4/17.
 *
 */

public class MailApplication extends Application {
    private static MailApplication instance;
    public MailApplication() throws Exception{
        if(instance==null){
            instance=this;
            ConfigManager.openConfig();
        }else {
            throw new Exception("Only one instance of application is possible");
        }
    }

    public static MailApplication getInstance(){
        if(instance==null){
            try {
                instance=new MailApplication();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }
}
