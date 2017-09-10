package com.osama.project34.ui.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
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
import com.osama.project34.ui.activities.about.AboutActivity;
import com.osama.project34.ui.activities.about.KeyDetailsActivity;
import com.osama.project34.MailApplication;
import com.osama.project34.R;
import com.osama.project34.data.Folder;
import com.osama.project34.data.Mail;
import com.osama.project34.data.Profile;
import com.osama.project34.imap.ConnectionManager;
import com.osama.project34.imap.FolderNames;
import com.osama.project34.imap.MailCallbacks;
import com.osama.project34.imap.MailManager;
import com.osama.project34.oauth.OauthCallbacks;
import com.osama.project34.oauth.OauthGmail;
import com.osama.project34.ui.fragments.MessagesFragment;
import com.osama.project34.utils.CommonConstants;
import com.osama.project34.utils.ConfigManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DataActivity extends BaseActivity implements MailCallbacks,OauthCallbacks {
    private static final String TAG=DataActivity.class.getName();

    private View                    rootContainer;
    private ActionBarDrawerToggle   drawerToggle;
    private DrawerLayout            mDrawerLayout;
    private Account mCurrentAccount;
    private String mAccountName;
    private AccountManager mAccountManager;
    private String mOauthToken;
    private ConnectionManager connectionManager;
    private NavigationView mNavView;
    private MessagesFragment[] messagesFragments;
    private MessagesFragment mFragment;
    private Toolbar toolbar;

    private static final int FIRST=0;
    private static final int SECOND=1;
    private static final int THIRD=2;
    private static final int FOURTH=3;
    private int previousSelectedPosition=0;

    private Folder[] folders;

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(CommonConstants.MESSAGE_FOLDER_ID)){
                //get message
                Log.d("bullhead", "onReceive: called");
                int folderId=intent.getIntExtra(CommonConstants.MESSAGE_FOLDER_ID,-1);
                if (folderId>messagesFragments.length || folderId==-1){
                    Log.e(TAG, "onReceive: folder id not possible");
                    return;
                }
                messagesFragments[folderId].updateMessages(MailApplication.getDb().
                        getAllMessages(folderId));
                messagesFragments[folderId].setLoading(intent.getBooleanExtra(CommonConstants.LOADING_INTENT,false));
            }
        }
    };
    private BroadcastReceiver messageNumberReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra(CommonConstants.MESSAGE_NUMBER_DATA)){
                int folderId=intent.getIntExtra(CommonConstants.MESSAGE_FOLDER_ID,-1);
                Log.d("bullhead", "onReceive: receiver broadcast to sent message number: "+folderId);
                if (folderId!=-1 && folderId<messagesFragments.length){
                    messagesFragments[folderId].setMessagesNumber(intent.getIntExtra(CommonConstants.MESSAGE_NUMBER_DATA,0));
                }
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

        initFragments();
        registerReceiver(receiver,new IntentFilter(CommonConstants.GOT_MESSAGE_BROADCAST));
        registerReceiver(messageNumberReceiver,new IntentFilter(CommonConstants.MESSAGE_NUMBER_BROADCAST));
        //get the currently logged in account.
        setupAccount();
        findViewById(R.id.floating_compose_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail();
            }
        });
        setupToolbarAndDrawer();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        unregisterReceiver(receiver);
        //      unregisterReceiver(messageNumberReceiver);
    }

    private void composeEmail() {
        Intent intent=new Intent(this,MailComposeActivity.class);
        startActivity(intent);
    }

    private void initFragments() {
        folders=new Folder[4];
        folders[FIRST]=new Folder(FolderNames.ID_INBOX,FolderNames.INBOX);
        folders[SECOND]=new Folder(FolderNames.ID_SENT,FolderNames.SENT);
        folders[THIRD]=new Folder(FolderNames.ID_DRAFT,FolderNames.DRAFT);
        folders[FOURTH]=new Folder(FolderNames.ID_FAVORITE,FolderNames.FAVORITE);

        messagesFragments = new MessagesFragment[4];
        messagesFragments[FIRST] = MessagesFragment.newInstance(folders[FIRST]);
        messagesFragments[SECOND] = MessagesFragment.newInstance(folders[SECOND]);
        messagesFragments[THIRD] = MessagesFragment.newInstance(folders[THIRD]);
        messagesFragments[FOURTH] = MessagesFragment.newInstance(folders[FOURTH]);

        //as five is local folder fragment so set its field
        ArrayList<Mail> data=MailApplication.getDb().getAllFavoriteMails();

        messagesFragments[FOURTH].setMessages(data);
        Log.d("bullhead", "initFragments: "+data.size());
        messagesFragments[FOURTH].setMessagesNumber(data.size());

        mFragment = messagesFragments[0];


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.data_content_frame, mFragment)
                .commit();

    }

    private void setupAccount() {
        mAccountName = getIntent().getExtras().getString(CommonConstants.DATA_ACTIVITY_INTENT_PERM);
        assert mAccountName != null;
        mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        Log.d(TAG, "setupAccount: should get account now: "+mOauthToken);
        for (Account acc :
                mAccountManager.getAccountsByType("com.google")) {
            Log.d(TAG, "setupAccount: getting this and that");
            if (acc.name.equalsIgnoreCase(mAccountName)) {
                Log.d(TAG, "setupAccount: current account is: "+mAccountName);
                this.mCurrentAccount = acc;
                ConfigManager.saveEmail(mAccountName);
                break;
            }
        }
        assert mCurrentAccount != null;
        new OauthGmail(mCurrentAccount,this);

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
        toolbar=(Toolbar)findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout)findViewById(R.id.activity_data);
        drawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        mNavView=(NavigationView)findViewById(R.id.main_navigation_drawer);
        setNavListener();
        setUpDrawerHeader();
    }

    private void setNavListener() {
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mNavView.getMenu().getItem(previousSelectedPosition).setChecked(false); //uncheck the previous selected item
                item.setChecked(true);
                switch (item.getItemId()){
                    case R.id.inbox_drawer_item:
                        changeFragment(FIRST);
                        break;
                    case R.id.sent_drawer_item:
                        changeFragment(SECOND);
                        break;
                    case R.id.drafts_drawer_item:
                        changeFragment(THIRD);
                        break;

                    case R.id.favorite_drawer_item:
                        changeFragment(FOURTH);
                        break;
                    case R.id.keydeatail_drawer_item:
                        Intent detail=new Intent(DataActivity.this, KeyDetailsActivity.class);
                        startActivity(detail);
                        item.setChecked(false);
                        break;
                    case R.id.About_drawer_item:
                        Intent about=new Intent(DataActivity.this, AboutActivity.class);
                        startActivity(about);
                        item.setChecked(false);
                        break;
                    case R.id.drawer_setting_item:
                        Intent settings=new Intent(DataActivity.this,SettingsActivity.class);
                        startActivity(settings);
                        item.setChecked(false);
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
    private void changeFragment(int position){
        if (position>messagesFragments.length) return;
        previousSelectedPosition=position;
        toolbar.setTitle(folders[position].getTitle());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.data_content_frame,messagesFragments[position])
                .commit();
    }

    private void setUpDrawerHeader() {
        View headerView=mNavView.getHeaderView(0);
        ((TextView)headerView.findViewById(R.id.user_account_name)).setText(mAccountName);
        Profile profile=ConfigManager.getProfile();
        ((TextView) headerView.findViewById(R.id.username_textview)).setText(profile.getName());
        Picasso.with(this)
                .load(profile.getImage())
                .placeholder(R.drawable.ic_account_circle)
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
