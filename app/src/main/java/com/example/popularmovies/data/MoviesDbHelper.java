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

    private static final int DATABASE_VERSION = 13;

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


        final String SQL_CREATE_MOVIES_TABLE =

                "CREATE TABLE " + MoviesEntry.TABLE_NAME + " ("
                        + MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL , "
                        + MoviesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "
                        + MoviesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL,"
                        + MoviesEntry.COLUMN_MOVIE_POSTER_LINK + " TEXT NOT NULL, "
                        + MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, "
                        + MoviesEntry.COLUMN_MOVIE_PLOT_SYNOPSIS + " TEXT NOT NULL "
                        + ")";


        final String SQL_CREATE_MOVIES_TABLE_FAV =
                "CREATE TABLE " + MoviesEntry.TABLE_NAME_FAV + " ("
                        + MoviesEntry.COLUMN_FAV_ID + " INTEGER PRIMARY KEY, "
                        + MoviesEntry.COLUMN_FAV_TITLE + " INTEGER, "
                        + MoviesEntry.COLUMN_FAV_RELEASE_DATE + " TEXT NOT NULL, "
                        + MoviesEntry.COLUMN_FAV_POSTER_LINK + " TEXT NOT NULL, "
                        + MoviesEntry.COLUMN_FAV_VOTE_AVERAGE + " TEXT NOT NULL, "
                        + MoviesEntry.COLUMN_FAV_PLOT_SYNOPSIS + " TEXT NOT NULL "
                        + ")";

        final String SQL_CREATE_MOVIE_TRAILERS_INFO =
                "CREATE TABLE " + MoviesEntry.TABLE_NAME_TRAILER + " ("
                        + MoviesEntry.COLUMN_TRAILER_ID + " TEXT, "
                        + MoviesEntry.COLUMN_TRAILER_NAME + " TEXT "
                        + ")";

        final String SQL_CREATE_MOVIE_REVIEWS_INFO =
                "CREATE TABLE " + MoviesEntry.TABLE_NAME_REVIEW + " ("
                        + MoviesEntry.COLUMN_REVIEW_AUTHOR + " TEXT, "
                        + MoviesEntry.COLUMN_REVIEW_CONTENT + " TEXT "
                        + ")";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE_FAV);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TRAILERS_INFO);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_REVIEWS_INFO);
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME_FAV);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME_TRAILER);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME_REVIEW);

        onCreate(sqLiteDatabase);
    }
}
