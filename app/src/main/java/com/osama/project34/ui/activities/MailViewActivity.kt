package com.osama.project34.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.google.gson.Gson
import com.osama.project34.R
import com.osama.project34.data.Mail
import com.osama.project34.utils.CommonConstants
import kotlinx.android.synthetic.main.activity_mail_view.*

class MailViewActivity : AppCompatActivity() {
    var currentMail:Mail?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_view)


        currentMail=Gson().fromJson(intent.extras.getString(CommonConstants.MAIL_VIEW_INTENT),Mail::class.java)
        viewerMailSubject.text=currentMail!!.subject
        senderName.text=currentMail!!.sender
        val generator = ColorGenerator.MATERIAL
        val thumb = TextDrawable.builder()
                .buildRound(currentMail!!.sender.substring(0, 1), generator.getColor(currentMail!!.id))
        viewerSenderIcon.setImageDrawable(thumb)
      for (data in currentMail!!.message.text){
           email_viewer.loadData(data,"text/html; charset=utf-8", "UTF-8")
      }
        setupToolbar()
        email_viewer.layoutParams.width=resources.displayMetrics.widthPixels

    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.main_toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }


}
