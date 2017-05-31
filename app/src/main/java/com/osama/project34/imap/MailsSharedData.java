package com.osama.project34.imap;

import android.accounts.Account;

import javax.mail.Session;
import javax.mail.Store;

/**
 * Created by bullhead on 5/31/17.
 *
 */

public class MailsSharedData {
    private static Session session;
    private static Store   store;
    private static Account userAccount;

    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        MailsSharedData.session = session;
    }

    public static Store getStore() {
        return store;
    }

    public static void setStore(Store store) {
        MailsSharedData.store = store;
    }

    public static Account getUserAccount() {
        return userAccount;
    }

    public static void setUserAccount(Account userAccount) {
        MailsSharedData.userAccount = userAccount;
    }
}
