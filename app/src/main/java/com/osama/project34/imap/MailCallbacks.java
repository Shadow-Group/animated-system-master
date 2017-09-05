package com.osama.project34.imap;

import android.accounts.Account;

import com.osama.project34.data.Mail;

import java.util.ArrayList;

/**
 * Created by home on 3/24/17.
 *
 */

public interface MailCallbacks {
    void connectionError(String error);
    void informConnectionStatus(boolean status);
    void gotTheMessage(Mail messages);
}
