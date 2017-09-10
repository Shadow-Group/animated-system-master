package com.osama.project34.imap;

import com.osama.project34.data.Mail;



public interface MailCallbacks {
    void connectionError(String error);

    void informConnectionStatus(boolean status);

    void gotTheMessage(Mail messages);
}
