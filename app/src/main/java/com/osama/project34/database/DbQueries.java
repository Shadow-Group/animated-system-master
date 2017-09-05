package com.osama.project34.database;


/**
 * Created by bullhead on 9/5/17.
 *
 */

 final class DbQueries {
     static final String CREATE_TABLE_FOLDERS=
            "CREATE TABLE "+FolderEntry.TABLE_NAME + " ("+
                    FolderEntry._ID+" INTEGER PRIMARY KEY,"+
                    FolderEntry.COLUMN_TITLE+" TEXT)";
     static final String CREATE_TABLE_MAILS=
            "CREATE TABLE "+MailEntry.TABLE_NAME+"("+
                    MailEntry._ID+" INTEGER PRIMARY KEY,"+
                    MailEntry.COLUMN_SUBJECT+" TEXT,"+
                    MailEntry.COLUMN_SENDER+" TEXT,"+
                    MailEntry.COLUMN_RECIPIENTS+ " TEXT,"+
                    MailEntry.COLUMN_ATTACHMENTS+ " TEXT,"+
                    MailEntry.COLUMN_MESSAGE+" TEXT,"+
                    MailEntry.COLUMN_ENCRYPT+" INTEGER,"+
                    MailEntry.COLUMN_FAVORITE+ " INTEGER,"+
                    MailEntry.COLUMN_READ_STATUS+" INTEGER,"+
                    MailEntry.COLUMN_FOLDER_ID+" INTEGER)";

     static final String CREATE_TABLE_KEY=
            "CREATE TABLE "+KeyEntry.TABLE_NAME+" ("+
                    KeyEntry._ID+" INTEGER PRIMARY KEY,"+
                    KeyEntry.COLUMN_EMAIL+" TEXT,"+
                    KeyEntry.COLUMN_KEY+" TEXT,"+
                    KeyEntry.COLUMN_VALID+" INTEGER)";


}
