package com.osama.project34.oauth;

import android.accounts.Account;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.osama.project34.MailApplication;
import com.osama.project34.utils.Constants;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by home on 3/4/17.
 *
 */

public class OauthGmail {
    private static final String TAG=OauthGmail.class.getCanonicalName();
    private static final String SCOPE="oauth2:https://mail.google.com/ https://www.googleapis.com/auth/userinfo.profile";

    private OauthCallbacks   callbacks;
    private Account          account;
    private String           mAccessToken;
    private Context          mContext;

    public OauthGmail(Account account,Context context){
        this.account=account;
        callbacks=(OauthCallbacks)context;
        this.mContext=context;

        new RetrieveTokenTask().execute(this.account);
    }

    private class RetrieveTokenTask extends AsyncTask<Account, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Account... params) {
            Account accountName = params[0];
            try {
                if (mAccessToken == null) {
                    Log.d(TAG, "doInBackground: getting token: " + accountName.name);
                    mAccessToken = GoogleAuthUtil.getToken(MailApplication.getInstance(), accountName, SCOPE);
                }
                Log.d(TAG, "doInBackground: got the token");
                //ConnectionManager emailManager = ConnectionManager.getInstance(accountName, mAccessToken);
                Log.d(TAG, "doInBackground: connection established");
                //save the account token in shared preferences
                SharedPreferences prefs =
                        MailApplication.getInstance().getSharedPreferences(
                                Constants.SHARED_PREFS_OAUTH, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                Constants.ACCESS_TOKEN=mAccessToken;
                editor.putString(accountName.name, mAccessToken);
                editor.putString(Constants.STRING_ACCOUNT_SHARED_PREFS,accountName.name);
                editor.apply();
                editor.commit();

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
                mAccessToken=e.getMessage();
                return false;
            } catch (UserRecoverableAuthException e) {
                callbacks.startSignInActivity(e);
            } catch (GoogleAuthException e) {
                Log.e(TAG, e.getMessage());
                mAccessToken=e.getMessage();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            if (s) {
                callbacks.tokenSuccessful(mAccessToken);
            } else {
                callbacks.tokenError(mAccessToken);
            }
        }
    }

}
