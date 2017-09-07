package com.osama.project34.imap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeMultipart;

/**
 * Created by bullhead on 9/7/17.
 *
 */

public class MultiPartHandler {
    private String[] text;
    private String[] attachments;

    public String[] getText() {
        return text;
    }

    public void setText(String[] text) {
        this.text = text;
    }


    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }
    public static MultiPartHandler createFromMessage(Message message) throws IOException, MessagingException {
            MultiPartHandler handler=new MultiPartHandler();
        try {

            Multipart multipart = (Multipart) message.getContent();
            ArrayList<String> text=new ArrayList<>();
        ArrayList<String> attachments=new ArrayList<>();
        for (int i = 0; i < multipart.getCount(); i++) {
            Part bodyPart=multipart.getBodyPart(i);
            if ( bodyPart.getDisposition()!=null && bodyPart.getDisposition().equalsIgnoreCase("ATTACHMENT")){
                DataHandler data=bodyPart.getDataHandler();
                attachments.add(data.getName());
            }
            text.add(getText(bodyPart));
        }
        handler.setAttachments(attachments.toArray(new String[attachments.size()]));
        handler.setText(text.toArray(new String[text.size()]));
        }catch (ClassCastException ex){
            ex.printStackTrace();
           handler.setText(new String[]{message.getContent().toString()});
            handler.setAttachments(new String[0]);
        }

        return  handler;
    }
    private static boolean textIsHtml = false;

    /**
     * Return the primary text content of the message.
     */
    private static String getText(Part p) throws
            MessagingException, IOException {
        if (p.isMimeType("text/*")) {
            String s = (String)p.getContent();
            textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart)p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null)
                        text = getText(bp);
                    continue;
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null)
                        return s;
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart)p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null)
                    return s;
            }
        }

        return null;
    }
    private static boolean hasAttachments(Message msg) throws MessagingException, IOException {
        if (msg.isMimeType("multipart/mixed")) {
            Multipart mp = (Multipart)msg.getContent();
            if (mp.getCount() > 1)
                return true;
        }
        return false;
    }
}
