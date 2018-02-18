package com.tpoint.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class DataDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tpoint.db";

    DataDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a table that holds the tpoints.
        final String SQL_CREATE_TPOINTS_TABLE = "CREATE TABLE " + DataContract.TpointEntry.TABLE_NAME + " (" +
                DataContract.TpointEntry._ID + " INTEGER PRIMARY KEY, " +
                DataContract.TpointEntry.COLUMN_FIREBASEID + " TEXT NOT NULL, " +
                DataContract.TpointEntry.COLUMN_LAT + " REAL NOT NULL, " +
                DataContract.TpointEntry.COLUMN_LON + " REAL NOT NULL, " +
                DataContract.TpointEntry.COLUMN_DISTANCE + " INTEGER NOT NULL, " +
                DataContract.TpointEntry.COLUMN_CREATION_DATE + " INTEGER NOT NULL, " +
                DataContract.TpointEntry.COLUMN_ARCHIVE_DATE + " INTEGER);";

        db.execSQL(SQL_CREATE_TPOINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and init over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        db.execSQL("DROP TABLE IF EXISTS " + DataContract.TpointEntry.TABLE_NAME);
        onCreate(db);
    }
}