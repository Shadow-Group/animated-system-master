package com.osama.project34.ui.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.osama.project34.R;
import com.osama.project34.imap.ConnectionManager;
import com.osama.project34.imap.ImapCallbacks;
import com.osama.project34.imap.LabelManager;
import com.osama.project34.imap.MessagesDataModel;
import com.osama.project34.utils.Constants;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity implements ImapCallbacks{
    private static final String TAG=DataActivity.class.getName();
    private static boolean LIGHT_THEME = false;
    private RecyclerView    mDataListView;
    private ArrayList<MessagesDataModel> mMessages;
    private View          rootContainer;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawerLayout;
    private Account mCurrentAccount;
    private String mAccountName;
    private AccountManager mAccountManager;
    private String mOauthToken;
    private ConnectionManager connectionManager;
    private NavigationView mNavView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //check if theme is light
        if (LIGHT_THEME) {
            super.setTheme(R.style.AppThemeLight);
        } else {
            super.setTheme(R.style.AppThemeDar);
        }
        rootContainer=getLayoutInflater().inflate(R.layout.activity_data,null);

        setContentView(rootContainer);
        setResult(RESULT_OK);

        Log.d(TAG, "onCreate: Created activity");
        ((FrameLayout)findViewById(R.id.data_content_frame)).addView(getLayoutInflater().inflate(R.layout.data_content_layout,null));
        //get the currently logged in account.
        setupAccount();

        setUpList();

        setupToolbarAndDrawer();
        animateWhat();


    }

    private void setupAccount() {
        mAccountName = getIntent().getExtras().getString(Constants.DATA_ACTIVITY_INTENT_PERM);
        mOauthToken = getIntent().getExtras().getString(Constants.DATA_ACTIVITY_PERM_TOKEN);
        assert mAccountName != null;
        mAccountManager = (AccountManager) getSystemService(ACCOUNT_SERVICE);
        for (Account acc :
                mAccountManager.getAccountsByType("com.google")) {
            if (acc.name.equalsIgnoreCase(mAccountName)) {
                this.mCurrentAccount = acc;
                break;
            }
        }
        assert mCurrentAccount != null;
        connectionManager = new ConnectionManager(mCurrentAccount, mOauthToken, this);

    }

    private void setUpList() {
        mDataListView=(RecyclerView)findViewById(R.id.messages_recycler_view);
        mDataListView.setLayoutManager(new LinearLayoutManager(this));
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
            case R.id.change_theme_menu_item: {
                //change theme
                //I need to recreate the activity to set theme
                //also change the menu item color
                toggleTheme();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (LIGHT_THEME) {
             menu.getItem(0).setIcon(R.drawable.ic_search_black_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_palette_black_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_search_white_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_palette_white_24dp);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    private void toggleTheme() {
        mMessages=null;
        mDataListView=null;
        LIGHT_THEME = !LIGHT_THEME;
        recreate();
    }


    //trying to animate something
    private void animateWhat(){
        Animation animation=new TranslateAnimation(0f,100f,0f,100f);
        mDataListView.setAnimation(animation);
        mDataListView.animate();
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

    new LabelManager(this);
}
    }

    @Override
    public void updateLabels(ArrayList<CharSequence> labels) {
       // Menu menu=mNavView.getMenu();
        for (CharSequence seq:
             labels) {
            Log.d(TAG, "updateLabels: label is: "+seq);
        }
    }
}
