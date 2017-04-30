package com.osama.project34.imap;

import javax.mail.Address;

/**
 * Created by home on 1/29/17.
 *
 */

public class MessagesDataModel {
    String   messageDate;
    String   messageNumber;
    String   messageSubject;
    String[] messageRecipients;

    //upon every new instance set the fields

    public MessagesDataModel(String messageDate, String messageNumber, String messageSubject, Address[] messageRecipients) {
        this.messageDate        = messageDate;
        this.messageNumber      = messageNumber;
        this.messageSubject     = messageSubject;
        this.messageRecipients  = new String[messageRecipients.length];
        int index=0;
        for (Address addr:messageRecipients) {
            this.messageRecipients[index++]=addr.toString();
        }
    }

    public String getMessageDate() {
        return messageDate;
    }

    public String getMessageNumber() {
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
