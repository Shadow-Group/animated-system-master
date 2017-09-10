package com.osama.project34.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.osama.project34.R;
import com.osama.project34.data.Key;
import com.osama.project34.encryption.EncryptionHandler;
import com.osama.project34.firebase.FirebaseHandler;
import com.osama.project34.imap.MailSendTask;
import com.osama.project34.utils.CommonConstants;
import com.osama.project34.utils.ConfigManager;
import com.osama.project34.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MailComposeActivity extends BaseActivity {

    private EditText sentToEdit;
    private EditText ccEdit;
    private EditText bccEdit;
    private EditText subjectEdit;
    private EditText composeEdit;
    private String attachmentPath;

    private boolean shouldEncrypt;
    private Key recipientKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        bindElements();

        //check if we are launched in forward or reply mode
        String action=getIntent().getAction();
        if (action!=null) {
            switch (action) {
                case CommonConstants.COMPOSE_INTENT_ACTION_FORWARD:
                    setupForward();
                    break;
                case CommonConstants.COMPOSE_INTENT_ACTION_REPLY:
                    setupReply();
            }
        }
    }

    private void setupForward() {
        String message=getIntent().getStringExtra(CommonConstants.COMPOSE_INTENT_EXTRA_MESSAGE);
        String subject=getIntent().getStringExtra(CommonConstants.COMPOSE_INTENT_EXTRA_SUBJECT);
        composeEdit.setText(message);
        subjectEdit.setText(subject);
    }

    private void setupReply() {
        String to=getIntent().getStringExtra(CommonConstants.COMPOSE_INTENT_EXTRA_MAIL);
        sentToEdit.setText(to);
        sentToEdit.setEnabled(false);
        //check if email can be used to encrypt
        for (Key key:FirebaseHandler.getInstance().getKeys()) {
            if (key.getUser().equals(to)){
                recipientKey = key;
                shouldEncrypt=true;
                break;
            }
            shouldEncrypt=false;
        }
        if (shouldEncrypt){
            ((ImageView) findViewById(R.id.encryption_status_image)).setColorFilter(Color.parseColor("#4caf50"));
        }else{
            ((ImageView) findViewById(R.id.encryption_status_image)).setColorFilter(Color.parseColor("#818181"));
        }
    }


    private void bindElements() {
        sentToEdit= (EditText) findViewById(R.id.to);
        ccEdit= (EditText) findViewById(R.id.Cc);
        bccEdit= (EditText) findViewById(R.id.Bcc);
        subjectEdit= (EditText) findViewById(R.id.Subject);
        composeEdit= (EditText) findViewById(R.id.Compose);

        EditText formEdit=((EditText) findViewById(R.id.from));
        formEdit.setText(ConfigManager.getEmail());
        formEdit.setEnabled(false);
        sentToEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus){
                    //. check if recipient has our key in our db
                    String email=sentToEdit.getText().toString();
                    for (Key key:FirebaseHandler.getInstance().getKeys()) {
                        if (key.getUser().equals(email)){
                            recipientKey = key;
                            shouldEncrypt=true;
                            break;
                        }
                        shouldEncrypt=false;
                    }
                    if (shouldEncrypt){
                        ((ImageView) findViewById(R.id.encryption_status_image)).setColorFilter(Color.parseColor("#4caf50"));
                    }else{
                        ((ImageView) findViewById(R.id.encryption_status_image)).setColorFilter(Color.parseColor("#818181"));
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose_manu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==R.id.action_send){
           sendMail();
        }

        return super.onOptionsItemSelected(item);
    }
    private static final int RC_CHOOSE_FILE=10;
    private void attachFile() {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,RC_CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_CHOOSE_FILE && resultCode==RESULT_OK) {
            String uri = data.getDataString();
            try{
                File file=new File(uri);
                InputStream in = getContentResolver().openInputStream(Uri.parse(uri));
                attachmentPath= FileUtils.createTempAttachment(in,file.getName());
        }catch (IOException ex){
                ex.printStackTrace();
            }

        }
    }

    private void sendMail() {
        //check for the fields
        final String to=sentToEdit.getText().toString();
        final String subject=subjectEdit.getText().toString();
        final String message=composeEdit.getText().toString();

        if (TextUtils.isEmpty(to)){
            sentToEdit.setError("Please input recipient mail");
            return;
        }
        if (TextUtils.isEmpty(subject)){
            new AlertDialog.Builder(this,ConfigManager.isDarkTheme()?R.style.DialogStyleDark:R.style.DialogStyleLight)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            confirmSend(to,subject,message);
                        }
                    })
                    .setTitle("Subject")
                    .setMessage("Send message without subject?")
                    .show();
            return;
        }
       confirmSend(to,subject,message);
    }

    private void confirmSend(final String to, final String subject, final String message) {
        final ProgressDialog dialog=new ProgressDialog(this,ConfigManager.isDarkTheme()?R.style.DialogStyleDark:R.style.DialogStyleLight);
        dialog.setMessage("Sending message. Please wait...");
        dialog.setTitle("Message");
        dialog.setCancelable(false);
        dialog.show();
        try {
            if (shouldEncrypt) {
                File fileToEncrypt = FileUtils.createFromString(message, "mess.pgp");
                Log.d("hello", "confirmSend: key is:  "+recipientKey.getText());
                File pubKeyFile = FileUtils.createFromString(recipientKey.getText(), "key.pub");
                Log.d("hello", "confirmSend: "+FileUtils.getStringFromFile(pubKeyFile));
                final File outputFile = FileUtils.createTempFile();

                EncryptionHandler.encryptFile(fileToEncrypt, outputFile, pubKeyFile, new EncryptionHandler.OnFileEncrypted() {
                    @Override
                    public void onSuccess() {
                       sendNow(to,subject,FileUtils.getStringFromFile(outputFile),dialog);
                    }

                    @Override
                    public void onError() {
                            dialog.dismiss();
                        Snackbar.make(sentToEdit,"Unable to send mail.",Snackbar.LENGTH_SHORT).show();
                    }
                });

            }
        }catch (IOException ex){
            ex.printStackTrace();
            dialog.dismiss();
            Snackbar.make(sentToEdit,"Unable to send mail.",Snackbar.LENGTH_SHORT).show();
        }

    }
    private void sendNow(String to, String subject, String message, final ProgressDialog dialog){
        MailSendTask.sendMail(to, subject, message, attachmentPath, shouldEncrypt,new MailSendTask.OnMailResponse() {
                            @Override
                            public void onSuccess() {
                                dialog.dismiss();
                                Snackbar.make(sentToEdit,"Mail sent.",Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                dialog.dismiss();

                                Snackbar bar = Snackbar.make(sentToEdit, "Unable to send mail.", Snackbar.LENGTH_SHORT);
                                bar.addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                    }
                                }).show();
                            }
                        });
    }
}
