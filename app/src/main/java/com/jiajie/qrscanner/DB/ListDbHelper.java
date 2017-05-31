package com.jiajie.qrscanner.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class ListDbHelper extends SQLiteOpenHelper {

    public  ListDbHelper(Context context) {
        super(context, ListContract.DB_NAME, null, ListContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = " CREATE TABLE " + ListContract.ScannedEntry.TABLE + " (" +
                ListContract.ScannedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ListContract.ScannedEntry.COL_RESULT + " TEXT NOT NULL, " +
                ListContract.ScannedEntry.LAT + " DOUBLE, " +
                ListContract.ScannedEntry.LNG + " DOUBLE); ";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + ListContract.ScannedEntry.TABLE);
        onCreate(db);
    }
}
