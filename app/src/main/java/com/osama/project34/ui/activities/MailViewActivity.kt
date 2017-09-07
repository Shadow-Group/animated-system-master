package com.osama.project34.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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
      for (data in currentMail!!.message.text){
           email_viewer.loadData(data,"text/html; charset=utf-8", "UTF-8")
      }
    }
}
