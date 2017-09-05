package com.osama.project34.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.osama.project34.data.Folder;
import com.osama.project34.data.Key;
import com.osama.project34.data.Mail;

import java.util.ArrayList;

/**
 * Created by bullhead on 9/5/17.
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME   = "sec";
    private static final int VERSION            = 1;
    private static DatabaseHelper instance;
    private DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance==null){
            instance=new DatabaseHelper(context);
        }
        return  instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //execute create queries
        sqLiteDatabase.execSQL(DbQueries.CREATE_TABLE_FOLDERS);
        sqLiteDatabase.execSQL(DbQueries.CREATE_TABLE_MAILS);
        sqLiteDatabase.execSQL(DbQueries.CREATE_TABLE_KEY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertMail(final Mail mail){
        ContentValues values=new ContentValues();
        values.put(MailEntry.COLUMN_SUBJECT,mail.getSubject());
        values.put(MailEntry.COLUMN_DATE,mail.getDate());
        values.put(MailEntry.COLUMN_ENCRYPT,mail.isEncrypted()?1:0);
        values.put(MailEntry.COLUMN_FAVORITE,mail.isFavorite()?1:0);
        values.put(MailEntry.COLUMN_READ_STATUS,mail.isReadStatus()?1:0);
        values.put(MailEntry.COLUMN_FOLDER_ID,mail.getFolderId());
        values.put(MailEntry.COLUMN_MESSAGE,mail.getMessage());
        values.put(MailEntry.COLUMN_RECIPIENTS,Util.makeString(mail.getRecipients()));
        values.put(MailEntry.COLUMN_SENDER,mail.getSender());

        SQLiteDatabase database=getWritableDatabase();
        database.insert(MailEntry.TABLE_NAME,null,values);
    }
    public void insertFolder(final Folder folder){
        ContentValues values=new ContentValues();
        values.put(FolderEntry.COLUMN_TITLE,folder.getTitle());

        SQLiteDatabase database=getWritableDatabase();
        database.insert(FolderEntry.TABLE_NAME,null,values);
    }
    public void insertKey(Key key){
        ContentValues values=new ContentValues();
        values.put(KeyEntry.COLUMN_EMAIL,key.getUser());
        values.put(KeyEntry.COLUMN_KEY,key.getText());

        SQLiteDatabase database=getWritableDatabase();
        database.insert(KeyEntry.TABLE_NAME,null,values);
    }
}
