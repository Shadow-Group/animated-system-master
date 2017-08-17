package com.osama.project34;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by Shadow on 6/17/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME="User.db";
    private static final int DATABASE_VERSION=1;

    //Table Names
    private static final String TABLE_NAME="Mails";
    private static final String TABLE_NAME1="Folders";

    //column Names for Mails table
    private static final String COLUMN_ID="ID";
    private static final String COLUMN_USER_NAME="Name";
    private static final String COLUMN_EMAIL="Email";
    private static final String COLUMN_SUBJECT="Subject";
    private static final String Column_Message="Message";

    //column names for Folders Table
    private static final String COLUMN_ID_1="ID";
    private static final String COLUMN_FOLDER_NAME="FolderName";



    private static final String TASK_TABLE_CREATE = "create table "
            + TABLE_NAME + " (" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_EMAIL
            + " text not null, " + COLUMN_USER_NAME + " text not null, "
            + COLUMN_SUBJECT + " text,"+Column_Message+"text not null , FOREIGN KEY(Table_id) REFERENCE"+TABLE_NAME1+"("+COLUMN_ID_1+");";

    private static final String TASK_TABLE_CREATE_1 = "create table "
            + TABLE_NAME1 + " (" + COLUMN_ID_1
            + " integer primary key autoincrement, " + COLUMN_FOLDER_NAME
            + "text Not null);";


    SQLiteDatabase db;
    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASK_TABLE_CREATE);
        db.execSQL(TASK_TABLE_CREATE_1);
        this.db=db;
    }


    public void insertFolderData(Data d)
    {
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        String query="select * from "+TABLE_NAME1 ;
        Cursor cursor=db.rawQuery(query,null);
        values.put(COLUMN_FOLDER_NAME,d.getFolderName());


        db.insert(TABLE_NAME1, null, values);
        db.close();
    }

    public void insertEmailsData(Data d)
    {
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        String query="select * from "+TABLE_NAME;
        Cursor cursor=db.rawQuery(query,null);
        values.put(COLUMN_EMAIL,d.getEmail());
        values.put(COLUMN_USER_NAME,d.getUserName());
        values.put(COLUMN_SUBJECT,d.getMessage());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }



    public void deleteEmail(String email)
    {
        db.execSQL("delete * from "+TABLE_NAME+" where "+COLUMN_EMAIL+ " = " +email);
    }

    public Cursor searchData(String table_Name,String email)
    {
        db=this.getReadableDatabase();
        String query="select * from "+table_Name+" where "+COLUMN_EMAIL+ " = "+email;
        Cursor cursor=db.rawQuery(query,null);
        String a,b;
        b="Not found";
        if(cursor.moveToFirst())
        {
            do{
                a=cursor.getString(1);
                if(a.equals(email))
                {
                    break;
                }

            }while (cursor.moveToNext());
        }
        return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
