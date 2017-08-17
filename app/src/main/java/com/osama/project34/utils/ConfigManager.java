package com.osama.project34.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.osama.project34.MailApplication;

/**
 * Created by bullhead on 8/17/17.
 *
 */

public final class ConfigManager {
    private static boolean darkTheme;
    private static SharedPreferences mConfigPrefs;

    public static boolean isDarkTheme() {
        return darkTheme;
    }
    public static void saveTheme(boolean isDark){
        mConfigPrefs.edit()
                .putBoolean(ConfigKeys.THEME,isDark)
                .apply();
    }


    /**
     * This must be called at the application start.
     * Maybe its better to call form @see com.osama.project34.MailApplication#onCreate()
     */
    public static void openConfig(){
        mConfigPrefs= MailApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
        darkTheme   = mConfigPrefs.getBoolean(ConfigKeys.THEME,false);
    }

    private static final class ConfigKeys{
        private static final String THEME="theme";
    }
}
