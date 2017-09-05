package com.osama.project34.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
