package com.osama.project34.oauth;


import com.google.android.gms.auth.UserRecoverableAuthException;



public interface OauthCallbacks {
    void tokenSuccessful(String token);

    void tokenError(String error);

    void startSignInActivity(UserRecoverableAuthException e);
}

