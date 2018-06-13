package com.example.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.popularmovies.data.MoviesContract.MoviesEntry;

/**
 * Created by rosha on 5/16/2018.
 */

public class MoviesDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "movies.db";

    /*
     * If you change the database schema, you must increment the database version or the onUpgrade
     * method will not be called.*/

    private static final int DATABASE_VERSION = 4;

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our movies data.
         */

        //TODO ADD columns to the table creation for (ids?) reviews and trailers
        //increment the db version above.

        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MoviesEntry.TABLE_NAME + " (" +

                        MoviesEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MoviesEntry.COLUMN_TITLE       + " TEXT NOT NULL, "                +

                        MoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL,"                +

                        MoviesEntry.COLUMN_MOVIE_BACKDROP_LINK + " TEXT NOT NULL, "         +

                        MoviesEntry.COLUMN_MOVIE_POSTER_LINK   + " TEXT NOT NULL, "        +

                        MoviesEntry.COLUMN_VOTE_AVERAGE   + " REAL NOT NULL, "             +

                        MoviesEntry.COLUMN_PLOT_SYNOPSIS   + " TEXT NOT NULL " + ")"           ;

        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
