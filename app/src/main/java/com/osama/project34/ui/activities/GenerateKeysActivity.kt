package com.osama.project34.ui.activities

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AlertDialog
import com.osama.project34.R
import com.osama.project34.encryption.EncryptionHandler
import com.osama.project34.utils.CommonConstants
import com.osama.project34.utils.ConfigManager
import com.osama.project34.utils.FileUtils
import kotlinx.android.synthetic.main.activities_toolbar.*
import kotlinx.android.synthetic.main.activity_generate_keys.*

class GenerateKeysActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //check if keys are generated
        if (ConfigManager.isKeyGenerated()) {
            startDataActivity()
            return
        }
        setContentView(R.layout.activity_generate_keys)
        setResult(Activity.RESULT_OK)
        main_toolbar.title = "Generate keys"
        button_generate_key.setOnClickListener({ generateKeys() })
        button_select_key.setOnClickListener({ selectKey() })
    }

    private fun selectKey() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                val filename = FileUtils.createTempAttachment(
                        contentResolver.openInputStream(Uri.parse(data!!.dataString)), "key.pgp")
                importKeys(filename)

            }
        }
    }

    private fun importKeys(secKeyFilename: String) {
        var style = R.style.DialogStyleLight
        if (ConfigManager.isDarkTheme()) {
            style = R.style.DialogStyleDark;
        }
        val dialog = ProgressDialog(this, style)
        dialog.setMessage("Generating keys. please wait....");
        dialog.setTitle("Keys Generation");
        dialog.setCancelable(false);
        dialog.show()
        val context = this
        EncryptionHandler.importKeys(secKeyFilename, object : EncryptionHandler.OnKeysGenerated {
            override fun onSuccess() {
                dialog.dismiss()
                AlertDialog.Builder(context)
                        .setPositiveButton("Ok", { dialog, which -> startDataActivity() })
                        .setCancelable(false)
                        .setMessage("Keys successfully generated.")
                        .show()
            }

            override fun onError() {
                Snackbar.make(button_select_key, "Unable to import keys.", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun generateKeys() {
        //show password dialog
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.passwords_fragment_layout)
        dialog.setTitle("keys password")
        val editText = dialog.findViewById(R.id.gen_keys_password_edit) as TextInputEditText
        val confirmEditText = dialog.findViewById(R.id.gen_keys_password_edit_confirm) as TextInputEditText
        dialog.show()

        dialog.findViewById(R.id.trigger_gen_key_button).setOnClickListener {
            //test the passwords
            if (isValidPassword(editText.text)) {
                if (editText.text.toString() == confirmEditText.text.toString()) {
                    //be sure to dismiss the dialog
                    dialog.dismiss()
                    //start the async task
                    generateKeys(editText.text.toString())
                } else {
                    confirmEditText.error = "Password does not match"
                }
            } else {
                editText.error = "Password should contain at least three characters"
            }
        }
        dialog.findViewById(R.id.cancel_dialog_button).setOnClickListener { dialog.dismiss() }
    }

    private fun isValidPassword(password: CharSequence): Boolean {
        return password.length > 3
    }

    private fun generateKeys(password: String) {
        var style = R.style.DialogStyleLight
        if (ConfigManager.isDarkTheme()) {
            style = R.style.DialogStyleDark;
        }
        val dialog = ProgressDialog(this, style)
        dialog.setMessage("Generating keys. please wait....");
        dialog.setTitle("Keys Generation");
        dialog.setCancelable(false);
        dialog.show()
        val context = this
        EncryptionHandler.generateKeys(ConfigManager.getEmail(), password, object : EncryptionHandler.OnKeysGenerated {

            override public fun onSuccess() {
                dialog.dismiss()
                AlertDialog.Builder(context)
                        .setPositiveButton("Ok", { dialog, which -> startDataActivity() })
                        .setCancelable(false)
                        .setMessage("Keys successfully generated.")
                        .show()
            }

            override public fun onError() {
                dialog.dismiss()
                AlertDialog.Builder(context)
                        .setPositiveButton("Retry", { dialog, which -> generateKeys(password) })
                        .setCancelable(false)
                        .setTitle("Error")
                        .setMessage("Unable to generate keys.")
                        .show()
            }
        })
    }

    private fun startDataActivity() {
        val intent = Intent(this, DataActivity::class.java)
        intent.putExtra(CommonConstants.DATA_ACTIVITY_INTENT_PERM, ConfigManager.getEmail())
        intent.putExtra(CommonConstants.DATA_ACTIVITY_PERM_TOKEN, CommonConstants.ACCESS_TOKEN)
        startActivityForResult(intent, 1)
        finish();
    }

}
