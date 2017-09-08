package com.osama.project34.imap;

import android.os.AsyncTask;

import com.osama.project34.utils.CommonConstants;
import com.osama.project34.utils.ConfigManager;
import com.sun.mail.smtp.SMTPTransport;

import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by bullhead on 9/6/17.
 *
 */

public class MailSendTask {
    public static final String OAUTH_TOKEN_PROP =
            "mail.imaps.sasl.mechanisms.oauth2.oauthToken";

    public static void sendMail(final String to, final String subject,
                                final String message, final String attachment, final boolean isEncrypted, final OnMailResponse callback){
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
//                    if (isEncrypted){
//                        message1.setText(CommonConstants.OUR_ID_FOR_ENCRYPTED+message);
//                    }else{
//                        message1.setText(message);
//                    }
                    BodyPart part=new MimeBodyPart();
                      MimeMultipart mimeMultipart;
                    if (isEncrypted){
                        mimeMultipart=new MimeMultipart("encrypted");
                    }else{
                        mimeMultipart=new MimeMultipart("alternative");
                    }
                    part.setText(message);
                    mimeMultipart.addBodyPart(part);

                    part.setHeader("Content-Type",MimeTypes.TEXT);
                    message1.setContent(mimeMultipart,MimeTypes.PGP);
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
    public interface OnMailResponse {
        void onSuccess();
        void onError();
    }
}
