package com.osama.project34.imap;

import android.os.AsyncTask;

import com.osama.project34.utils.ConfigManager;

import java.util.Date;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by bullhead on 9/6/17.
 *
 */

public class MailSendTask {
    public static void sendMail(final String to,final String message,
                                final String subject,final String attachment,final OnMailResponce callback){
        new AsyncTask<Void,Void,Boolean>(){

            @Override
            protected Boolean doInBackground(Void... voids) {
                try {
                    Message message1 = new MimeMessage(MailsSharedData.getSession());
                    message1.setFrom(new InternetAddress(ConfigManager.getProfile().getMail()));
                    message1.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                    message1.setSubject(subject);
                    message1.setSentDate(new Date());
                    message1.setText(message);
                    Transport.send(message1);
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
