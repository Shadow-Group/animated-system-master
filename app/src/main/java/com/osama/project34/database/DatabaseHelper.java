package com.osama.project34.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.osama.project34.data.Folder;
import com.osama.project34.data.Key;
import com.osama.project34.data.Mail;
import com.osama.project34.imap.MessagesManager;
import com.osama.project34.imap.MultiPartHandler;

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
            ContentValues values = new ContentValues();
            values.put(MailEntry._ID, mail.getId());
        values.put(MailEntry.COLUMN_MESSAGE_NUMEBR,mail.getMessageNumber());
            values.put(MailEntry.COLUMN_SUBJECT, mail.getSubject());
            values.put(MailEntry.COLUMN_DATE, mail.getDate());
            values.put(MailEntry.COLUMN_ENCRYPT, mail.isEncrypted() ? 1 : 0);
            values.put(MailEntry.COLUMN_FAVORITE, mail.isFavorite() ? 1 : 0);
            values.put(MailEntry.COLUMN_READ_STATUS, mail.isReadStatus() ? 1 : 0);
            values.put(MailEntry.COLUMN_FOLDER_ID, mail.getFolderId());
            values.put(MailEntry.COLUMN_MESSAGE, new Gson().toJson(mail.getMessage()));
            values.put(MailEntry.COLUMN_RECIPIENTS, Util.makeString(mail.getRecipients()));
            values.put(MailEntry.COLUMN_SENDER, mail.getSender());

            SQLiteDatabase database = getWritableDatabase();
            database.insertWithOnConflict(MailEntry.TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
    }
    public long getLastMessageId(Folder folder){
        String selection=MailEntry.COLUMN_FOLDER_ID+" =?";
        String[] selectionArgs=new String[]{""+folder.getId()};
        Cursor cursor=getWritableDatabase().query(MailEntry.TABLE_NAME,
                new String[]{""+MailEntry._ID},selection,selectionArgs,null,null,null);
        long result=-1;
        if (cursor.moveToFirst()){
           result= cursor.getLong(0);
        }else{
            result= -1;
        }
        cursor.close();
        return result;
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
    public ArrayList<Mail> getAllMessages(int folderId){
        String selection=MailEntry.COLUMN_FOLDER_ID+" =?";
        String[] selectionArgs={String.valueOf(folderId)};
        String[] projection={
                MailEntry._ID,
                MailEntry.COLUMN_SUBJECT,
                MailEntry.COLUMN_SENDER,
                MailEntry.COLUMN_RECIPIENTS,
                MailEntry.COLUMN_MESSAGE,
                MailEntry.COLUMN_ENCRYPT,
                MailEntry.COLUMN_FAVORITE,
                MailEntry.COLUMN_READ_STATUS,
                MailEntry.COLUMN_FOLDER_ID,
                MailEntry.COLUMN_DATE,
                MailEntry.COLUMN_MESSAGE_NUMEBR
        };
        String orderBy=MailEntry.COLUMN_MESSAGE_NUMEBR+" DESC";
        Cursor cursor=getWritableDatabase().query(
                MailEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
               null,
                null,
                orderBy
                );
        ArrayList<Mail> data=new ArrayList<>();
        while (cursor.moveToNext()){
            Mail mai=new Mail();
            mai.setId(cursor.getLong(0));
            mai.setDate(cursor.getString(MailEntry.Indices.COLUMN_DATE));
            mai.setSender(cursor.getString(MailEntry.Indices.COLUMN_SENDER));
            mai.setEncrypted(cursor.getInt(MailEntry.Indices.COLUMN_ENCRYPT) != 0);
            mai.setFavorite(cursor.getInt(MailEntry.Indices.COLUMN_FAVORITE)!=0);
            mai.setReadStatus(cursor.getInt(MailEntry.Indices.COLUMN_READ_STATUS)!=0);
            mai.setFolderId(folderId);
            mai.setRecipients(Util.makeArrayListFromString(cursor.getString(MailEntry.Indices.COLUMN_RECIPIENTS)));
            mai.setMessage(new Gson().fromJson(cursor.getString(MailEntry.Indices.COLUMN_MESSAGE), MultiPartHandler.class));
            mai.setSubject(cursor.getString(MailEntry.Indices.COLUMN_SUBJECT));
            data.add(mai);
        }
        cursor.close();
        return data;
    }
    public boolean hasMessage(long uid){
        String selection=MailEntry._ID +" =?";
        String[] selectionArgs={String.valueOf(uid)};
        Cursor cursor=getWritableDatabase().query(
                MailEntry.TABLE_NAME,
                new String[]{MailEntry.COLUMN_MESSAGE_NUMEBR},
                selection,
                selectionArgs,
                null,
                null,
                null
                );
        if (cursor.moveToFirst()){
            cursor.close();
            return true;
        }
        return false;
    }

    public void updateMessageNumber(int messageNumber, long id) {
        String query="update "+MailEntry.TABLE_NAME+" set "+
                MailEntry.COLUMN_MESSAGE_NUMEBR+" ="+messageNumber +
                " WHERE "+MailEntry._ID+"="+id;
        getWritableDatabase().execSQL(query);
    }
    public long getMailCount(){
        return DatabaseUtils.queryNumEntries(getWritableDatabase(),MailEntry.TABLE_NAME);
    }
}
