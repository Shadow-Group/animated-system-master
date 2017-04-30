package com.osama.project34.oauth;


import com.google.android.gms.auth.UserRecoverableAuthException;

/**
 * Created by home on 3/4/17.
 *
 */

public interface OauthCallbacks {
    void tokenSuccessful(String token);
    void tokenError(String error);
    void startSignInActivity(UserRecoverableAuthException e);
}

