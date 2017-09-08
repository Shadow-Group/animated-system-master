package com.osama.project34.ui.activities

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.google.gson.Gson
import com.osama.project34.MailApplication
import com.osama.project34.R
import com.osama.project34.data.Mail
import com.osama.project34.imap.MultiPartHandler
import com.osama.project34.ui.widgets.EmailViewer
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
        email_viewer.setWebViewClient(EmailViewer(mail_loading_bar))
        LoadMailTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
        setupToolbar()

    }

    private fun setupToolbar() {
        val toolbar = findViewById(R.id.main_toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    private inner class LoadMailTask : AsyncTask<Void, String, MultiPartHandler>() {
        override fun doInBackground(vararg p0: Void?): MultiPartHandler? {
            return try {
                //load mail content
                val message: MultiPartHandler = MailApplication.getDb().getMessageContent(currentMail!!.id)
                for (data in message.text) {
                    publishProgress(data)
                }
                message
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
        }

        override fun onProgressUpdate(vararg data: String) {
            email_viewer.loadData(data[0], "text/html; charset=utf-8", "UTF-8")
        }

        override fun onPostExecute(result: MultiPartHandler?) {
            super.onPostExecute(result)
            if (result != null) {
                currentMail!!.message = result
            } else {
                //show unable to load mail
            }
        }

    }


}
