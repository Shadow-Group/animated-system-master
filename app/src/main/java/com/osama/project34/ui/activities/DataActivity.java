package com.osama.project34.ui.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.UserRecoverableAuthException;
import com.osama.project34.MailApplication;
import com.osama.project34.R;
import com.osama.project34.data.Mail;
import com.osama.project34.imap.ConnectionManager;
import com.osama.project34.imap.MailCallbacks;
import com.osama.project34.imap.MailManager;
import com.osama.project34.imap.MessagesDataModel;
import com.osama.project34.oauth.OauthCallbacks;
import com.osama.project34.oauth.OauthGmail;
import com.osama.project34.data.Profile;
import com.osama.project34.ui.fragments.MessagesFragment;
import com.osama.project34.utils.ConfigManager;
import com.osama.project34.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataActivity extends BaseActivity implements MailCallbacks,OauthCallbacks {
    private static final String TAG=DataActivity.class.getName();

    private View                    rootContainer;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawerLayout;
    private Account mCurrentAccount;
    private String mAccountName;
    private AccountManager mAccountManager;
    private String mOauthToken;
    private ConnectionManager connectionManager;
    private NavigationView mNavView;
    private MessagesFragment[] messagesFragments;
    private MessagesFragment mFragment;

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("bullhead", "onReceive: outsite the if in broadcast receiver");
            if (intent.hasExtra(Constants.MESSAGE_FOLDER_ID)){
                //get message TODO
                Log.d("bullhead", "onReceive: called");
                mFragment.updateMessages(MailApplication.getDb().
                        getAllMessages(intent.getIntExtra(Constants.MESSAGE_FOLDER_ID,0)));
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rootContainer=getLayoutInflater().inflate(R.layout.activity_data,null);

        setContentView(rootContainer);
        setResult(RESULT_OK);

        Log.d(TAG, "onCreate: Created activity");
        messagesFragments=new MessagesFragment[4];
        messagesFragments[0]=new MessagesFragment();
        messagesFragments[1]=new MessagesFragment();
        messagesFragments[2]=new MessagesFragment();
        messagesFragments[3]=new MessagesFragment();
        registerReceiver(receiver,new IntentFilter(Constants.GOT_MESSAGE_BROADCAST));

        mFragment=messagesFragments[0];
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.data_content_frame,mFragment)
                .commit();
        //get the currently logged in account.
        setupAccount();
        setupToolbarAndDrawer();

    }

    private void setupAccount() {
        mAccountName = getIntent().getExtras().getString(Constants.DATA_ACTIVITY_INTENT_PERM);
        assert mAccountName != null;
        mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Log.d(TAG, "setupAccount: should get account now: "+mOauthToken);
        for (Account acc :
                mAccountManager.getAccountsByType("com.google")) {
            Log.d(TAG, "setupAccount: getting this and that");
            if (acc.name.equalsIgnoreCase(mAccountName)) {
                Log.d(TAG, "setupAccount: current account is: "+mAccountName);
                this.mCurrentAccount = acc;
                break;
            }
        }
        assert mCurrentAccount != null;
        new OauthGmail(mCurrentAccount,this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu
        getMenuInflater().inflate(R.menu.data_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
           /* case R.id.change_theme_menu_item: {
                //change theme
                //I need to recreate the activity to set theme
                //also change the menu item color
                toggleTheme();
                break;
            }*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
            menu.getItem(0).setIcon(R.drawable.ic_search_white_24dp);
         //   menu.getItem(1).setIcon(R.drawable.ic_palette_white_24dp);
        return super.onPrepareOptionsMenu(menu);
    }
    private void toggleTheme() {
        ConfigManager.saveTheme(!ConfigManager.isDarkTheme());
        recreate();
    }


    private void setupToolbarAndDrawer() {
        Toolbar toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.activity_data);
        drawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        mNavView=(NavigationView)findViewById(R.id.main_navigation_drawer);
        setUpDrawerHeader();
    }

    private void setUpDrawerHeader() {
        View headerView=mNavView.getHeaderView(0);
        ((TextView)headerView.findViewById(R.id.user_account_name)).setText(mAccountName);
        Profile profile=ConfigManager.getProfile();
        ((TextView) headerView.findViewById(R.id.username_textview)).setText(profile.getName());
        Picasso.with(this)
                .load(profile.getImage())
                .into(((ImageView) headerView.findViewById(R.id.user_account_icon)));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void connectionError(String error) {
        Snackbar.make(rootContainer,getResources().getString(R.string.error_connect,error),Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void informConnectionStatus(boolean status) {
        if(status){
            MailManager.Companion.getInstance(this).getObjectFactory().getMessageManager().checkMessages();
        }
    }

    @Override
    public void gotTheMessage(Mail messages) {
    //  mFragment.updateMessages(messages);

    }
    private void setProfile(){

    }

    @Override
    public void tokenSuccessful(String token) {
        mOauthToken=token;
        connectionManager = new ConnectionManager(mCurrentAccount, mOauthToken, this);
    }

    @Override
    public void tokenError(String error) {
        Snackbar.make(rootContainer,"There is an error.",Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void startSignInActivity(UserRecoverableAuthException e) {

    }
}
