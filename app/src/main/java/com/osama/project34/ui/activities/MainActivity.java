package com.osama.project34.ui.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.osama.project34.R;
import com.osama.project34.data.Profile;
import com.osama.project34.oauth.OauthCallbacks;
import com.osama.project34.oauth.OauthGmail;
import com.osama.project34.ui.adapters.AccountsAdapter;
import com.osama.project34.ui.adapters.AdapterCallbacks;
import com.osama.project34.utils.CommonConstants;
import com.osama.project34.utils.CommonUtils;
import com.osama.project34.utils.ConfigManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends BaseActivity implements AdapterCallbacks, OauthCallbacks {
    private static final String TAG = MainActivity.class.getName();

    private static final int REQ_PERMISSION = 90;
    private static final int REQ_SIGN_IN_REQUIRED = 10;
    private static final int REQ_ADD_ACCOUNT = 32;


    private Account[] mAllAccounts;
    private String[] mUserAccountsAddress;
    private RecyclerView mAccountsList;
    private AccountsAdapter mAccountsAdapter;
    private Account mCurrentAccount;
    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        listAccounts();

    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listAccounts();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void viewClicked(int position) {
        //check the google play services availability
        if (!isRequiredService()) {
            //show dialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, ConfigManager.isDarkTheme() ? R.style.DialogStyleDark : R.style.DialogStyleLight);
            builder.setMessage(R.string.play_services_failed_message);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final String appPackageName = "com.google.android.gms";
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                }
            });
            builder.show();
            return;
        }

        if (position >= mAllAccounts.length) {
            // this means add new account
            addAccount();
            return;
        }
        //set selected account
        mCurrentAccount = mAllAccounts[position];
        //check if I already have the token
        if (mAccessToken != null) {
            showGenerateKeysDialog();
        } else {
            getToken(position);
        }

    }

    private boolean isRequiredService() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQ_ADD_ACCOUNT: {
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: Account successfully added");
                    //reload the account list
                    listAccounts();
                    mAccountsAdapter.setData(mUserAccountsAddress);
                    mAccountsAdapter.notifyDataSetChanged();
                    mAccountsList.requestLayout();
                }
                break;
            }
            case REQ_SIGN_IN_REQUIRED: {
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "onActivityResult: Got the permissions");
                    //start the email getting operation
                    //TODO
                }
                break;
            }
        }
    }

    /**
     * miscellaneous
     */
    private void toggleTheme() {
        ConfigManager.saveTheme(!ConfigManager.isDarkTheme());
        recreate();
    }


    private void listAccounts() {
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            getOrCheckPermissions();
            return;
        }

        int index = 0;
        mAllAccounts = manager.getAccountsByType("com.google");
        mUserAccountsAddress = new String[mAllAccounts.length];

        for (Account account : mAllAccounts) {
            if (account.type.equalsIgnoreCase("com.google")) {
                Log.d(TAG, "listAccounts: account is: " + account.name);
                mUserAccountsAddress[index++] = account.name;
            }
        }
        setupList();
    }

    private void setupList() {
        mAccountsList = (RecyclerView) findViewById(R.id.user_accounts_listview);
        mAccountsList.setLayoutManager(new LinearLayoutManager(this));

        mAccountsAdapter = new AccountsAdapter(this, mUserAccountsAddress);
        mAccountsList.setAdapter(mAccountsAdapter);
        animateWhat();
    }

    private void getOrCheckPermissions() {
        String[] perms = {Manifest.permission.GET_ACCOUNTS};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perms, REQ_PERMISSION);
        }
    }

    private void addAccount() {
        Log.d(TAG, "addAccount: Adding new account upon user request");
        Intent addAccountIntent = new Intent(android.provider.Settings.ACTION_ADD_ACCOUNT)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[]{"com.google"});
        startActivityForResult(addAccountIntent, REQ_ADD_ACCOUNT);
    }

    /**
     * Task for setup section
     */
    ProgressDialog mProgressDialog;

    @Override
    public void tokenSuccessful(String token) {
        //save email in config
        ConfigManager.saveEmail(mCurrentAccount.name);
        mAccessToken = token;
        getProfileInfo();


    }

    private void getProfileInfo() {
        new AsyncTask<Void, Void, Profile>() {
            @Override
            protected Profile doInBackground(Void... params) {
                try {
                    String url = "https://www.googleapis.com/plus/v1/people/me?access_token=" + mAccessToken + "&key=" + CommonConstants.API_KEY;
                    String json = CommonUtils.getJsonfromUrl(url);
                    JSONObject rootObject = new JSONObject(json);
                    Profile profile = new Profile();
                    profile.setName(rootObject.getString("displayName"));
                    profile.setMail(mCurrentAccount.name);
                    profile.setImage(rootObject.getJSONObject("image").getString("url"));
                    return profile;
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(Profile profile) {
                super.onPostExecute(profile);
                if (profile != null) {
                    mProgressDialog.dismiss();
                    ConfigManager.saveProfileInfo(profile);
                    showGenerateKeysDialog();
                } else {
                    mProgressDialog.dismiss();
                    showGenerateKeysDialog();
                }
            }
        }.execute();
    }

    private void showGenerateKeysDialog() {
        startActivityForResult(new Intent(this, GenerateKeysActivity.class), 100);
        finish();
    }

    @Override
    public void tokenError(String error) {
        mProgressDialog.dismiss();
        Snackbar.make(
                findViewById(R.id.activity_main),
                "There is an error. " + error,
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void startSignInActivity(UserRecoverableAuthException e) {
        startActivityForResult(e.getIntent(), REQ_SIGN_IN_REQUIRED);
    }

    private void getToken(int pos) {
        mProgressDialog = new ProgressDialog(MainActivity.this, ConfigManager.isDarkTheme() ? R.style.DialogStyleDark : R.style.DialogStyleLight);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Account setup");
        mProgressDialog.setMessage("Setting up account please wait");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new OauthGmail(mAllAccounts[pos], this);
    }


    //trying to animate something
    private void animateWhat() {
        Animation animation = new ScaleAnimation(0f, 1f, 0f, 1f);
        animation.setDuration(800);
        mAccountsList.setAnimation(animation);
        mAccountsList.animate();
    }
}
