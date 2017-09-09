package com.osama.project34.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.osama.project34.R
import com.osama.project34.utils.ConfigManager
import kotlinx.android.synthetic.main.activities_toolbar.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitch=prefs_theme_box;
        themeSwitch.isChecked=ConfigManager.isDarkTheme()
        main_toolbar.title = "Settings"
        themeSwitch.setOnClickListener({
            themeSwitch.isChecked=!themeSwitch.isChecked;
            switchTheme();
        })
    }

    private fun switchTheme() {
        if (ConfigManager.isDarkTheme()){
            ConfigManager.saveTheme(false)
        }else{
            ConfigManager.saveTheme(true)
        }
        recreate()
    }
}
