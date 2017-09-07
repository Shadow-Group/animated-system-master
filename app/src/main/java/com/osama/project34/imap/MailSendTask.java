package com.osama.project34.imap;

import android.os.AsyncTask;

import com.osama.project34.data.Key;
import com.osama.project34.encryption.EncryptionHandler;
import com.osama.project34.utils.ConfigManager;
import com.osama.project34.utils.CommonConstants;
import com.osama.project34.utils.FileUtils;
import com.sun.mail.smtp.SMTPTransport;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by bullhead on 9/6/17.
 *
 */

public class MailSendTask {
    public static final String OAUTH_TOKEN_PROP =
            "mail.imaps.sasl.mechanisms.oauth2.oauthToken";

    public static void sendMail(final String to, final String subject,
                                final String message, final String attachment, final boolean isEncrypted, final OnMailResponce callback){
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {

                    Properties props=System.getProperties();
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.starttls.required", "true");
                    props.put("mail.smtp.ssl.enable", "true");
                    props.put("mail.smtp.auth.mechanisms", "XOAUTH2");
                    Session session = Session.getInstance(props);
                    session.setDebug(true);


                    Message message1 = new MimeMessage(MailsSharedData.getSession());
                    message1.setFrom(new InternetAddress(ConfigManager.getEmail()));
                    message1.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    message1.setSubject(subject);
                    message1.setSentDate(new Date());
                    message1.setText(message);
                    if (isEncrypted){
                        message1.setFlags(new PgpFlag(),true);
                    }
                    final URLName unusedUrlName = null;

                    SMTPTransport transport = new SMTPTransport(session, unusedUrlName);

                    transport.connect("smtp.gmail.com",465,ConfigManager.getEmail(), CommonConstants.ACCESS_TOKEN);
                    transport.sendMessage(message1,message1.getAllRecipients());
                    return true;
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
                    callback.onSuccess();
                }else{
                    callback.onError();
                }
            }
        }.execute();

    }
    public interface OnMailResponce{
        void onSuccess();
        void onError();
    }
}
