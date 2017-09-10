package com.osama.project34.imap;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.osama.project34.MailApplication;
import com.osama.project34.R;
import com.osama.project34.data.Mail;
import com.osama.project34.data.Sender;
import com.osama.project34.utils.ConfigManager;

import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

/**
 * Created by bullhead on 9/10/17.
 */

public class NotificationService extends Service {
    private static final String TAG = "bullhead_service";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: service oncreated called");
        new Thread(messageCheckingRunnable).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private Runnable messageCheckingRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Log.d(TAG, "run: service started");
                final Folder inbox = FolderNames.getImapFolder(FolderNames.ID_INBOX);
                while (true) {
                    Thread.sleep(1500);
                    Message[] messages = checkMail(inbox);
                    if (messages != null) {
                        for (int i = 0; i < messages.length; i++) {
                            {
                                Message message = messages[i];
                                if (message != null) {
                                    long uid = ((UIDFolder) inbox).getUID(message);
                                    Mail mail = makeMailFromMessage(message, FolderNames.ID_INBOX, uid);
                                    showNotification(mail);
                                }
                            }
                        }
                    }
                    inbox.close();
                }
            } catch (InterruptedException | IOException | MessagingException e) {
                e.printStackTrace();
            }
        }
    };

    private Message[] checkMail(Folder folder) throws MessagingException {
        if (!folder.isOpen()) {
            folder.open(Folder.READ_WRITE);
        }
        SearchTerm search = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
        Message[] messages = folder.search(search);
        Message[] unseenMessages = new Message[messages.length];
        int count = 0;
        for (Message message : messages) {
            long uid = ((UIDFolder) folder).getUID(message);
            if (MailApplication.getDb().hasMessage(uid)) {
                Log.d(TAG, "no new mail");
            } else {
                Log.d(TAG, "checkMail: got a new message");
                unseenMessages[count++] = message;
            }
        }
        return unseenMessages;

    }

    private void showNotification(Mail mail) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentText("New mail arrived.")
                .setContentTitle(mail.getSender().getName() == null ? mail.getSender().getMail() : mail.getSender().getName())
                .setSmallIcon(R.drawable.logo);
        manager.notify(0, notification.build());
    }

    private Mail makeMailFromMessage(Message message, int folderId, long messageUid) throws MessagingException, IOException {
        Mail model = new Mail();
        model.setMessageNumber(message.getMessageNumber());
        model.setFolderId(folderId);
        model.setFavorite(false);
        model.setId(messageUid);
        model.setEncrypted(message.getContentType().toLowerCase().contains("multipart/encrypted"));
        model.setSubject(message.getSubject());
        if (folderId == 0) {
            model.setDate(message.getReceivedDate().toString());
        } else {
            model.setDate(message.getSentDate().toString());
        }
        model.setMessage(MultiPartHandler.createFromMessage(message));
        if (message.getFrom() != null && message.getFrom().length > 0) {
            InternetAddress address = ((InternetAddress) message.getFrom()[0]);
            Sender sender = new Sender();
            sender.setMail(address.getAddress());
            sender.setName(address.getPersonal());
            model.setSender(sender);
        } else {
            Sender sender = new Sender();
            sender.setMail(ConfigManager.getEmail());
            sender.setName("Drafts");
            model.setSender(sender);
        }
        model.setReadStatus(message.isSet(Flags.Flag.SEEN));

        ArrayList<String> recipientAddresses = new ArrayList<String>();
        if (message.getAllRecipients() != null) {
            for (Address address : message.getAllRecipients()) {
                InternetAddress realAddress = (InternetAddress) address;
                recipientAddresses.add(realAddress.getAddress());
            }
        } else {
            recipientAddresses.add("Draft"); //drafts don't have recipient
        }
        model.setRecipients(recipientAddresses);
        return model;

    }
}
