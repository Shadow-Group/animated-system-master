package com.osama.project34.ui.activities

import android.app.Dialog
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.widget.EditText
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.google.gson.Gson
import com.osama.project34.MailApplication
import com.osama.project34.R
import com.osama.project34.data.Mail
import com.osama.project34.encryption.EncryptionHandler
import com.osama.project34.imap.MultiPartHandler
import com.osama.project34.ui.widgets.EmailViewer
import com.osama.project34.utils.CommonConstants
import com.osama.project34.utils.FileUtils
import kotlinx.android.synthetic.main.activity_mail_view.*
import java.io.File

class MailViewActivity : AppCompatActivity() {
    var currentMail:Mail?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_view)
        setupToolbar()

        currentMail=Gson().fromJson(intent.extras.getString(CommonConstants.MAIL_VIEW_INTENT),Mail::class.java)


        viewerMailSubject.text=currentMail!!.subject
        senderName.text=currentMail!!.sender
        val generator = ColorGenerator.MATERIAL
        val thumb = TextDrawable.builder()
                .buildRound(currentMail!!.sender.substring(0, 1), generator.getColor(currentMail!!.id))
        viewerSenderIcon.setImageDrawable(thumb)

        //check if email is encrypted
        if (currentMail!!.isEncrypted) {
            showDecryptDialog()
        } else {
            loadMail()
        }


    }

    private fun showDecryptDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_decrypt_mail)
        dialog.findViewById(R.id.generate_key_button).setOnClickListener({
            val editText = dialog.findViewById(R.id.key_password) as EditText
            val password = editText.text.toString()
            if (TextUtils.isEmpty(password)) {
                editText.error = "Password length must be greater than 3"
            } else {
                dialog.dismiss()
                decryptMail(password)
            }

        })
        dialog.show()
    }

    private fun decryptMail(password: String) {
        val decryptedFile = FileUtils.createTempFile()
        val message = MailApplication.getDb().getMessageContent(currentMail!!.id)
        var text = ""
        for (data in message.text) {
            text += data
        }
        val encryptedFile = FileUtils.createFromString(text, "message.pgp")
        val pubKeyFile = File(filesDir, "pub.asc")
        val secKeyFile = File(filesDir, "sec.asc")


        EncryptionHandler.decryptFile(encryptedFile, decryptedFile, pubKeyFile, secKeyFile, password,
                object : EncryptionHandler.OnFileDecrypted {
                    override fun onSuccess() {
                        email_viewer.loadData(FileUtils.getStringFromFile(decryptedFile), "text/html; charset=utf-8", "UTF-8")
                    }

                    override fun onError() {
                        Snackbar.make(email_viewer, "Unable to decrypt message.", Snackbar.LENGTH_LONG).show()

                    }
                })
    }

    private fun loadMail() {
        email_viewer.setWebViewClient(EmailViewer(mail_loading_bar))
        LoadMailTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
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
