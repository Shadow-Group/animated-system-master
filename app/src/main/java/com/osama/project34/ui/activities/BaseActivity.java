package com.osama.project34.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.osama.project34.R;
import com.osama.project34.utils.ConfigManager;



public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(ConfigManager.isDarkTheme() ? R.style.AppThemeDar : R.style.AppThemeLight);
    }
}
