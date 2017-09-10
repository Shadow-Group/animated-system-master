package com.osama.project34.imap;

import javax.mail.Folder;
import javax.mail.MessagingException;


public final class FolderNames {
    public static final String INBOX = "inbox";
    public static final String FAVORITE = "Favorites";
    public static final String SENT = "sent";
    public static final String DRAFT = "drafts";
    public static final String TRASH = "trash";

    public static final int ID_INBOX = 0;
    public static final int ID_FAVORITE = 4;
    public static final int ID_SENT = 1;
    public static final int ID_DRAFT = 2;
    public static final int ID_TRASH = 3;
    public static final String GMAIL = "[Gmail]";

    public static final class ImapNames {
        public static final String DRAFTS = "Drafts";
        public static final String SENT = "Sent Mail";
        public static final String TRASH = "Trash";
        public static final String INBOX = "INBOX";
    }

    public static Folder getImapFolder(int localFolderId) throws MessagingException {
        Folder folder;
        switch (localFolderId) {
            case ID_INBOX:
                folder = MailsSharedData.getStore().getDefaultFolder().getFolder(ImapNames.INBOX);
                break;
            case ID_DRAFT:
                folder = MailsSharedData.getStore().getDefaultFolder().getFolder(GMAIL).getFolder(ImapNames.DRAFTS);
                break;
            case ID_SENT:
                folder = MailsSharedData.getStore().getDefaultFolder().getFolder(GMAIL).getFolder(ImapNames.SENT);
                break;
            case ID_TRASH:
                folder = MailsSharedData.getStore().getDefaultFolder().getFolder(TRASH);
                break;
            default:
                throw new MessagingException("no folder found");
        }
        return folder;
    }

}
