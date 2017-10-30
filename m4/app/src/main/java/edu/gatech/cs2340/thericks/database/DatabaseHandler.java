package edu.gatech.cs2340.thericks.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Singleton class that handles database connection.  Provides a single connection throughout the
 * application's life cycle.
 *
 * Created by Ben Lashley on 10/9/2017.
 */

class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rat_data.db";

    private static DatabaseHandler instance = new DatabaseHandler(RatTrackerApplication.getAppContext());

    /**
     *
     * @return writable database
     */
    static synchronized SQLiteDatabase provideWritableDatabase() {
        return instance.getWritableDatabase();
    }

    /**
     *
     * @param context
     */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        RatDataDAO.onCreate(sqLiteDatabase);
    }

    /**
     *
     * @param sqLiteDatabase
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        RatDataDAO.onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }
}
