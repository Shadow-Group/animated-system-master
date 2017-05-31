package com.osama.project34.imap;

import android.accounts.Account;

import java.util.ArrayList;

/**
 * Created by home on 3/24/17.
 *
 */

public interface MailCallbacks {
    void connectionError(String error);
    void informConnectionStatus(boolean status);
    void updateLabels(ArrayList<CharSequence> labels);
}
