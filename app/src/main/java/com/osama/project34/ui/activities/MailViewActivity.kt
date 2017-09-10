package com.osama.project34.ui.activities

import android.app.Dialog
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.google.gson.Gson
import com.like.LikeButton
import com.like.OnLikeListener
import com.osama.project34.MailApplication
import com.osama.project34.R
import com.osama.project34.data.Mail
import com.osama.project34.encryption.EncryptionHandler
import com.osama.project34.imap.MultiPartHandler
import com.osama.project34.ui.widgets.EmailViewer
import com.osama.project34.utils.CommonConstants
import com.osama.project34.utils.ConfigManager
import com.osama.project34.utils.FileUtils
import kotlinx.android.synthetic.main.activity_mail_view.*
import java.io.File

class MailViewActivity : BaseActivity() {
    var currentMail: Mail? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mail_view)
        setupToolbar()

        currentMail = Gson().fromJson(intent.extras.getString(CommonConstants.MAIL_VIEW_INTENT), Mail::class.java)

        viewerMailSubject.text = currentMail!!.subject
        to.text = ConfigManager.getEmail()
        messageSendDate.text = currentMail!!.date
        likeButtonViewer.isLiked = currentMail!!.isFavorite

        likeButtonViewer.setOnLikeListener(object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                toggleFavorite(currentMail!!, p0!!)
            }

            override fun unLiked(p0: LikeButton?) {
                toggleFavorite(currentMail!!, p0!!)
            }

        })

        senderName.text = currentMail!!.sender.name ?: currentMail!!.sender.mail
        senderEmail.text = currentMail!!.sender.mail

        val generator = ColorGenerator.MATERIAL
        val name = currentMail!!.sender.name ?: currentMail!!.sender.mail
        val thumb = TextDrawable.builder()
                .buildRound(name.substring(0, 1), generator.getColor(currentMail!!.id))
        viewerSenderIcon.setImageDrawable(thumb)

        //check if email is encrypted
        if (currentMail!!.isEncrypted) {
            showDecryptDialog()
        } else {
            loadMail()
        }

        moreOptionsMenu.setOnClickListener({
            showPopupMenu(moreOptionsMenu)
        })

    }

    private fun showPopupMenu(anchorView: View) {
        val popMenu = PopupMenu(this, anchorView)
        popMenu.menuInflater.inflate(R.menu.menu_mail_options, popMenu.menu)
        popMenu.show()
        popMenu.setOnMenuItemClickListener { item ->
            when (item!!.itemId) {
                R.id.mail_reply_menu_item -> {
                    launchReplyActivity()
                }
                R.id.mail_forward_menu_item -> {
                    launchForwardActivity()
                }
            }
            true
        }
    }

    private fun launchReplyActivity() {
        val intent = Intent(this, MailComposeActivity::class.java)
        intent.action = CommonConstants.COMPOSE_INTENT_ACTION_REPLY
        intent.putExtra(CommonConstants.COMPOSE_INTENT_EXTRA_MAIL, currentMail!!.sender.mail)
        startActivity(intent)
    }

    private fun launchForwardActivity() {
        if (!currentMail!!.isEncrypted) {
            Snackbar.make(moreOptionsMenu, "currently only encrypted can be forward.", Snackbar.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, MailComposeActivity::class.java)
        intent.action = CommonConstants.COMPOSE_INTENT_ACTION_FORWARD
        intent.putExtra(CommonConstants.COMPOSE_INTENT_EXTRA_SUBJECT, currentMail!!.subject)
        intent.putExtra(CommonConstants.COMPOSE_INTENT_EXTRA_MESSAGE, currentMail!!.message.text[0])
        startActivity(intent)
    }


    private fun showDecryptDialog() {
        var style = R.style.DialogStyleLight
        if (ConfigManager.isDarkTheme()) {
            style = R.style.DialogStyleDark
        }

        val dialog = Dialog(this, style)
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

    private fun toggleFavorite(message: Mail, itemView: LikeButton) {
        if (message.isFavorite) {
            //message is already favorite
            message.isFavorite = false
            itemView.setLiked(false)
            MailApplication.getDb().removeFromFavorite(message) //remove message from favorites
            Snackbar.make(itemView, "Mail removed from favorite", Snackbar.LENGTH_LONG).setAction("Undo") {
                message.isFavorite = false
                toggleFavorite(message, itemView)
            }.show()
        } else {
            MailApplication.getDb().insertFavorite(message)
            message.isFavorite = true
            itemView.isLiked = true
        }
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
                        mail_loading_bar.visibility = View.GONE
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
