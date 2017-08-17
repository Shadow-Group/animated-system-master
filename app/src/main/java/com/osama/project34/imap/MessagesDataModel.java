package com.osama.project34.imap;

import javax.mail.Address;

/**
 * Created by home on 1/29/17.
 *
 */

public class MessagesDataModel {
    private String   messageDate;
    private int   messageNumber;
    private String   messageSubject;
    private String[] messageRecipients;
    private String   messageSender;
    private String   messageText;


    //upon every new instance set the fields

    public MessagesDataModel(String messageDate,String messageSender, int messageNumber,
                             String messageSubject,String messageText, Address[] messageRecipients) {
        this.messageDate        = messageDate;
        this.messageNumber      = messageNumber;
        this.messageSubject     = messageSubject;
        this.messageSender      = messageSender;
        this.messageText        = messageText;
        this.messageRecipients  = new String[messageRecipients.length];
        int index=0;
        for (Address addr:messageRecipients) {
            this.messageRecipients[index++]=addr.toString();
        }
    }

    public String getMessageSender() {
        return messageSender;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getMessageSubject() {
        return messageSubject;
    }

    public String[] getMessageRecipients() {
        return messageRecipients;
    }

    public int getNumberOfRecipients() {
        return messageRecipients.length;
    }
}
