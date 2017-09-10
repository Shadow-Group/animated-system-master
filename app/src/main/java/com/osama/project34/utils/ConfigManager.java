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
    public static boolean showMailEnabled(){
        return mConfigPrefs.getBoolean(ConfigKeys.SHOW_MAIL,false);
    }
    public static void setShowMail(boolean value){
        mConfigPrefs.edit()
                .putBoolean(ConfigKeys.SHOW_MAIL,value)
                .apply();
    }
    public static boolean isFirstRun(){
        return mConfigPrefs.getBoolean(ConfigKeys.FIRST_RUN,true);
    }
    public static void voidFirstRun(){
        mConfigPrefs.edit().putBoolean(ConfigKeys.FIRST_RUN,false).apply();
    }
    public static void saveProfileInfo(Profile profile){
        mConfigPrefs.edit()
                .putString(ConfigKeys.FULL_NAME,profile.getName())
                .putString(ConfigKeys.IMAGE_URL,profile.getImage())
                .putString(ConfigKeys.EMAIL,profile.getMail())
                .apply();
        ConfigManager.profile=profile;

    }
    public static void saveEmail(final String email){
        mConfigPrefs.edit()
                .putString(ConfigKeys.EMAIL,email)
                .apply();
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
        profile.setMail(mConfigPrefs.getString(ConfigKeys.EMAIL,null));
    }
    public static String getEmail(){
        return mConfigPrefs.getString(ConfigKeys.EMAIL,"");
    }

    private static final class ConfigKeys{
        private static final String THEME="theme";
        private static final String FULL_NAME="full_name";
        private static final String EMAIL="user_email";
        private static final String IMAGE_URL="image_url";
        private static final String FIRST_RUN="first_run";
        private static final String SHOW_MAIL="show_mail";
    }
}
