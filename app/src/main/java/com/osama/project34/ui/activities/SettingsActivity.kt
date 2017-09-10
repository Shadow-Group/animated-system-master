package com.osama.project34.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.osama.project34.R
import com.osama.project34.utils.ConfigManager
import kotlinx.android.synthetic.main.activities_toolbar.*
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitch=prefs_theme_box
        themeSwitch.isChecked=ConfigManager.isDarkTheme()
        prefs_show_mail_box.isChecked=ConfigManager.showMailEnabled()
        main_toolbar.title = "Settings"
        themeSwitch.setOnClickListener({
            switchTheme()
        })
        prefs_show_mail_box.setOnClickListener({
            switchShowMail()
        })
    }

    private fun switchShowMail() {
        if (ConfigManager.showMailEnabled()){
            ConfigManager.setShowMail(false)
        }else{
            ConfigManager.setShowMail(true)
        }
        prefs_show_mail_box.isChecked=ConfigManager.showMailEnabled()
    }

    private fun switchTheme() {
        if (ConfigManager.isDarkTheme()){
            ConfigManager.saveTheme(false)
        }else{
            ConfigManager.saveTheme(true)
        }
        Toast.makeText(this,"Theme may work after re-launch",Toast.LENGTH_LONG).show();
        recreate()
    }
}
