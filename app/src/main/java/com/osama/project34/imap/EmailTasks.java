package com.osama.project34.imap;

import android.os.AsyncTask;

import com.osama.project34.MailApplication;
import com.osama.project34.data.Mail;
import com.sun.mail.imap.protocol.FLAGS;
import com.sun.mail.pop3.POP3Folder;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;

/**
 * Created by bullhead on 9/9/17.
 *
 */

public class EmailTasks {
    public static void deleteMail(final Mail mail){
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    MailApplication.getDb().deleteMail(mail); //first delete from db
                    //delete from remote folder
                    Folder currentFolder=FolderNames.getImapFolder(mail.getFolderId());
                    currentFolder.open(Folder.READ_WRITE);
                    UIDFolder uidFolder= ((UIDFolder) currentFolder);
                    Message message=uidFolder.getMessageByUID(mail.getId());
                    Flags deleted=new Flags(Flags.Flag.DELETED);
                    currentFolder.setFlags(new Message[]{message},deleted,true);
                    currentFolder.expunge();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
