package com.osama.project34.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.osama.project34.MailApplication;
import com.osama.project34.data.Profile;

/**
 * Created by bullhead on 8/17/17.
 *
 */

public final class ConfigManager {
    private static boolean darkTheme;
    private static SharedPreferences mConfigPrefs;
   private static Profile profile;

    public static boolean isDarkTheme() {
        return darkTheme;
    }
    public static void saveTheme(boolean isDark){
        mConfigPrefs.edit()
                .putBoolean(ConfigKeys.THEME,isDark)
                .apply();
        darkTheme=isDark;
    }
    public static void saveProfileInfo(Profile profile){
        mConfigPrefs.edit()
                .putString(ConfigKeys.FULL_NAME,profile.getName())
                .putString(ConfigKeys.IMAGE_URL,profile.getImage())
                .apply();
        ConfigManager.profile=profile;

    }

    public static Profile getProfile() {
        return profile;
    }

    /**
     * This must be called at the application start.
     * Maybe its better to call form @see com.osama.project34.MailApplication#onCreate()
     */
    public static void openConfig(){
        mConfigPrefs= MailApplication.getInstance().getSharedPreferences("config", Context.MODE_PRIVATE);
        darkTheme   = mConfigPrefs.getBoolean(ConfigKeys.THEME,false);
        profile=new Profile();
        profile.setImage(mConfigPrefs.getString(ConfigKeys.IMAGE_URL,null));
        profile.setName(mConfigPrefs.getString(ConfigKeys.FULL_NAME,null));
    }

    private static final class ConfigKeys{
        private static final String THEME="theme";
        private static final String FULL_NAME="full_name";
        private static final String IMAGE_URL="image_url";
    }
}
