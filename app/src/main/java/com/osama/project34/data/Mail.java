package com.osama.project34.data;

import com.osama.project34.imap.MultiPartHandler;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by bullhead on 9/5/17.
 */

public class Mail implements Serializable {
    private long id;
    private int folderId;
    private boolean readStatus;
    private boolean encrypted;
    private ArrayList<String> recipients;
    private boolean favorite;
    private MultiPartHandler message;
    private String subject;
    private Sender sender;
    private String date;
    private int messageNumber;

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public MultiPartHandler getMessage() {
        return message;
    }

    public void setMessage(MultiPartHandler message) {
        this.message = message;
    }

    public void setMessage(String text) {
        this.message = new MultiPartHandler();
        this.message.setText(new String[]{text});
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
