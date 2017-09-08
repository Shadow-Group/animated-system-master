package com.osama.project34.database;

import android.provider.BaseColumns;

/**
 * Created by bullhead on 9/5/17.
 *
 */

public final class MailEntry implements BaseColumns {
    public static final String TABLE_NAME           = "mails";
    public static final String COLUMN_SUBJECT       = "subject";
    public static final String COLUMN_SENDER        = "sender";
    public static final String COLUMN_RECIPIENTS    = "recipients";
    public static final String COLUMN_MESSAGE       = "message";
    public static final String COLUMN_ENCRYPT       = "encrypted";
    public static final String COLUMN_FAVORITE      = "favorite";
    public static final String COLUMN_READ_STATUS   = "read_status";
    public static final String COLUMN_FOLDER_ID     = "folder_id";
    public static final String COLUMN_DATE          = "date";
    public static final String COLUMN_MESSAGE_NUMBER = "message_number";
    public static final class Indices {
        public static final int COLUMN_SUBJECT          = 1;
        public static final int COLUMN_SENDER           = 2;
        public static final int COLUMN_RECIPIENTS       = 3;
        public static final int COLUMN_MESSAGE          = 4;
        public static final int COLUMN_ENCRYPT          = 5;
        public static final int COLUMN_FAVORITE         = 6;
        public static final int COLUMN_READ_STATUS      = 7;
        public static final int COLUMN_FOLDER_ID        = 8;
        public static final int COLUMN_MESSAGE_NUMBER   = 10;
        public static final int COLUMN_DATE             = 9;
    }
}