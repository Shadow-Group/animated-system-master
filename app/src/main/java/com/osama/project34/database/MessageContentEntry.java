package com.osama.project34.database;

import android.provider.BaseColumns;

/**
 * Created by bullhead on 9/8/17.
 *
 */

public class MessageContentEntry implements BaseColumns {
    public static final String TABLE_NAME = "content";
    public static final String COLUMN_CONTENT = "message_content";
    public static final String COLUMN_MAIL_ID = "mail_id";

    public static final class Indices {
        public static final int COLUMN_CONTENT = 1;
        public static final int COLUMN_MAIL_ID = 2;
    }

}
