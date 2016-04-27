package com.framgia.rssfeed.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by VULAN on 4/26/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private String create_table = "CREATE TABLE [News]" +
            "(title TEXT, imageUrl TEXT,link TEXT,description TEXT)";
    private String drop_table = "DROP TABLE IF EXITS [News]";
    private static final String DATABASE_NAME = "db_history_news";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 10);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(drop_table);
        onCreate(db);
    }
}
