package com.osama.project34.ui.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.osama.project34.R;
import com.osama.project34.imap.MailSendTask;
import com.osama.project34.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MailComposeActivity extends AppCompatActivity {

    private EditText sentToEdit;
    private EditText ccEdit;
    private EditText bccEdit;
    private EditText subjectEdit;
    private EditText composeEdit;
    private String attachmentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        bindElements();
    }

    private void bindElements() {
        sentToEdit= (EditText) findViewById(R.id.to);
        ccEdit= (EditText) findViewById(R.id.Cc);
        bccEdit= (EditText) findViewById(R.id.Bcc);
        subjectEdit= (EditText) findViewById(R.id.Subject);
        composeEdit= (EditText) findViewById(R.id.Compose);
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

        if(id==R.id.action_attach_file){
            attachFile();
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
            new AlertDialog.Builder(this)
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

    private void confirmSend(String to, String subject, String message) {
        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Sending message. Please wait...");
        dialog.setTitle("Message");
        dialog.setCancelable(false);
        dialog.show();
        MailSendTask.sendMail(to, subject, message, attachmentPath, new MailSendTask.OnMailResponce() {
            @Override
            public void onSuccess() {
                dialog.dismiss();
            }

            @Override
            public void onError() {
                dialog.dismiss();
            }
        });
    }
}
