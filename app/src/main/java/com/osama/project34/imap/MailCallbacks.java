package com.osama.project34.imap;

import com.osama.project34.data.Mail;

/**
 * Created by home on 3/24/17.
 */

public interface MailCallbacks {
    void connectionError(String error);

    void informConnectionStatus(boolean status);

    void gotTheMessage(Mail messages);
}
