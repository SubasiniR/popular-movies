package com.example.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.popularmovies.data.MoviesContract.MoviesEntry;


/**
 * Created by rosha on 5/16/2018.
 */

public class MoviesProvider extends ContentProvider {

    public static final int CODE_MOVIES_LIST = 100;
    public static final int CODE_MOVIES_FAV = 200;
    public static final int CODE_MOVIE_DETAILS = 300;
    public static final int CODE_MOVIES_LIST_ID = 400;
    public static final int CODE_MOVIES_FAV_ID = 500;
    public static final int CODE_TRAILERS = 600;
    public static final int CODE_REVIEWS = 700;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private MoviesDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, MoviesContract.PATH_MOVIES, CODE_MOVIES_LIST);

        matcher.addURI(authority, MoviesContract.PATH_MOVIES + "/#", CODE_MOVIES_LIST_ID);

        matcher.addURI(authority, MoviesContract.PATH_MOVIES_FAV, CODE_MOVIES_FAV);

        matcher.addURI(authority, MoviesContract.PATH_MOVIES_FAV + "/#", CODE_MOVIES_FAV_ID);

        matcher.addURI(authority, MoviesContract.PATH_TRAILERS, CODE_TRAILERS);

        matcher.addURI(authority, MoviesContract.PATH_REVIEWS, CODE_REVIEWS);


        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        db.beginTransaction();
        int rowsInserted = 0;
        try {
            for (ContentValues value : values) {

                long _id;

                switch (sUriMatcher.match(uri)) {

                    case CODE_MOVIES_LIST:

                        _id = db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, value);
                        break;

                    case CODE_TRAILERS:
                        _id = db.insert(MoviesEntry.TABLE_NAME_TRAILER, null, value);
                        break;

                    case CODE_REVIEWS:
                        _id = db.insert(MoviesEntry.TABLE_NAME_REVIEW, null, value);
                        break;

                    default:
                        _id = -1;
                }
                if (_id != -1) {
                    rowsInserted++;

                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        if (rowsInserted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsInserted;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES_LIST: {

                cursor = db.query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);

                break;
            }

            case CODE_MOVIES_LIST_ID: {

                selection = MoviesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);

                break;
            }

            case CODE_MOVIES_FAV: {

                cursor = db.query(
                        MoviesContract.MoviesEntry.TABLE_NAME_FAV,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);

                break;
            }

            case CODE_MOVIES_FAV_ID: {

                selection = MoviesEntry.COLUMN_FAV_ID + " = ? ";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(
                        MoviesEntry.TABLE_NAME_FAV,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }

            case CODE_TRAILERS: {

                cursor = db.query(
                        MoviesEntry.TABLE_NAME_TRAILER,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }

            case CODE_REVIEWS: {

                cursor = db.query(
                        MoviesEntry.TABLE_NAME_REVIEW,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;

        if (null == selection) {
            selection = "1";
        }
        switch (sUriMatcher.match(uri)) {

            case CODE_MOVIES_LIST:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesContract.MoviesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            case CODE_MOVIES_FAV:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesEntry.TABLE_NAME_FAV,
                        selection,
                        selectionArgs);
                break;

            case CODE_TRAILERS:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesEntry.TABLE_NAME_TRAILER,
                        selection,
                        selectionArgs);
                Log.e("trailerRows deleted: ", numRowsDeleted+"");
                break;

            case CODE_REVIEWS:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        MoviesEntry.TABLE_NAME_REVIEW,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new RuntimeException("Method not implemented");

        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Method not implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        Uri uriOfRowInserted = null;
        long _id;

        switch (sUriMatcher.match(uri)) {
            case CODE_MOVIES_FAV:
                _id = mOpenHelper.getWritableDatabase()
                        .insert(MoviesEntry.TABLE_NAME_FAV,
                                null,
                                contentValues);
                break;
            default:
                throw new RuntimeException("Method not implemented");

        }

        if (_id != -1) {
            uriOfRowInserted = Uri.parse(String.valueOf(uri))
                    .buildUpon()
                    .appendPath(String.valueOf(_id))
                    .build();
        }

        return uriOfRowInserted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new RuntimeException("Method not implemented");
    }
}
