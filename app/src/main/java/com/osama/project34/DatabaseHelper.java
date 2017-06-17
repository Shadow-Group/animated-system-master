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

    private static final String Database_Name="User.db";
    private static final int Database_Version=1;
    private static final String Table_Name="Mails";
    private static final String Table_Name1="Folders";
    private static final String Column_ID="ID";
    private static final String Column_User_Name="Name";
    private static final String Column_Email="Email";
    private static final String Column_Subject="Subject";
    private static final String Column_Message="Message";

    private static final String Column_ID_1="ID";
    private static final String Column_Folder_Name="FolderName";



    private static final String TASK_TABLE_CREATE = "create table "
            + Table_Name + " (" + Column_ID
            + " integer primary key autoincrement, " + Column_Email
            + " text not null, " + Column_User_Name + " text not null, "
            + Column_Email + " text not null,"
            + Column_Subject + " text,"+Column_Message+"text not null , FOREIGN KEY(Table_id) REFERENCE"+Table_Name1+"("+Column_ID_1+");";

    private static final String TASK_TABLE_CREATE_1 = "create table "
            + Table_Name1 + " (" + Column_ID_1
            + " integer primary key autoincrement, " + Column_Folder_Name
            + "text Not null);";


    SQLiteDatabase db;
    public DatabaseHelper(Context context) {
        super(context,Database_Name,null, Database_Version);
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
        String query="select * from "+Table_Name1;
        Cursor cursor=db.rawQuery(query,null);
        values.put(Column_Folder_Name,d.getFolder_Name());


        db.insert(Table_Name1, null, values);
        db.close();
    }

    public void insertEmailsData(Data d)
    {
        db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        String query="select * from "+Table_Name;
        Cursor cursor=db.rawQuery(query,null);
        values.put(Column_Email,d.getEmail());
        values.put(Column_User_Name,d.getUser_Name());
        values.put(Column_Subject,d.getMessage());

        db.insert(Table_Name, null, values);
        db.close();
    }

    public void deleteEmail(String email)
    {
        db.execSQL("delete * from "+Table_Name+" where "+Column_Email+ " = " +email);
    }

    public Cursor searchData(String table_Name,String email)
    {
        db=this.getReadableDatabase();
        String query="select * from "+table_Name+" where "+Column_Email+ " = "+email;
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
