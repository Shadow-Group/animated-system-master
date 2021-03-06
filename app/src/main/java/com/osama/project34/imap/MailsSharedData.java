package com.osama.project34.imap;

import android.accounts.Account;

import java.util.ArrayList;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;



public class MailsSharedData {
    private static Session session;
    private static Store store;
    private static Account userAccount;
    private static ArrayList<Folder> allFolders = new ArrayList<>();

    public static void setAllFolders(ArrayList<Folder> allFolders) {
        MailsSharedData.allFolders = allFolders;
    }

    public static ArrayList<Folder> getAllFolders() {
        return allFolders;
    }

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

    public static void addFolder(Folder folder) {
        allFolders.add(folder);
    }
}
