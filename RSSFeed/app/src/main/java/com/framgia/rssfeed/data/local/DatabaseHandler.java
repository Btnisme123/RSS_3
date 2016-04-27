package com.framgia.rssfeed.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.framgia.rssfeed.bean.News;

import java.util.ArrayList;

/**
 * Created by VULAN on 4/26/2016.
 */
public class DatabaseHandler {

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private final String TABLE = "News";
    private final String TITLE = "title";
    private final String IMAGE_URL = "imageUrl";
    private final String LINK = "link";
    private final String DESCRIPTION = "description";
    private static DatabaseHandler sInstance;

    public static DatabaseHandler getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DatabaseHandler.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseHandler(context);
                }
            }
        }
        return sInstance;
    }

    public DatabaseHandler(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();

    }

    public void close() throws SQLException {
        mDatabaseHelper.close();
    }

    public void insertNewsInfo(News news) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, news.getTitle());
        contentValues.put(IMAGE_URL, news.getImageUrl());
        contentValues.put(LINK, news.getLink());
        contentValues.put(DESCRIPTION, news.getDescription());
        mSQLiteDatabase.insert(TABLE, null, contentValues);
        close();
    }

    public ArrayList<News> getHistoryNews() {
        open();
        ArrayList<News> newsList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("select * from [News] ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            News news = new News();
            news.setTitle(cursor.getString(0));
            news.setImageUrl(cursor.getString(1));
            news.setLink(cursor.getString(2));
            news.setDescription(cursor.getString(3));
            newsList.add(news);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return newsList;

    }

}
