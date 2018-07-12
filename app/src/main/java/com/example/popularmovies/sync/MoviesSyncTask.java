package com.example.popularmovies.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.utilities.NetworkUtils;
import com.example.popularmovies.utilities.OpenMoviesJsonUtils;

import java.net.URL;

/**
 * Created by rosha on 5/17/2018.
 */

public class MoviesSyncTask {

    private static final String TAG = MoviesSyncTask.class.getSimpleName();

    synchronized public static void syncMovies(Context context, String sortPreference) {

        try {
            /*
             * The getUrl method will return the URL that we need to get the JSON for the
             * Movies of user selected sort preference
             */
            URL moviesRequestUrl = NetworkUtils.getUrl(context, sortPreference);


            /* Use the URL to retrieve the JSON */
            String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);


            /* Parse the JSON into a list of movies content values */
            ContentValues[] moviesContentValues = OpenMoviesJsonUtils
                    .getMoviesContentValuesFromJson(context, jsonMoviesResponse);


            if (moviesContentValues != null && moviesContentValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver moviesContentResolver = context.getContentResolver();

                moviesContentResolver.delete(MoviesContract.MoviesEntry.CONTENT_URI, null, null);

                /* Insert our new data into movies's ContentProvider */
                moviesContentResolver.bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI, moviesContentValues);

            }

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }

    public static void getTrailerInfo(Context context, String movieIdString, String path) {

        try {

            URL trailerUrl = NetworkUtils.getTrailerAndReviewUrl(movieIdString, path);

            String trailerJsonString = NetworkUtils.getResponseFromHttpUrl(trailerUrl);

            ContentValues[] trailerContentValues = OpenMoviesJsonUtils.getTrailerContentValuesFromJson(trailerJsonString);

            if (trailerContentValues != null && trailerContentValues.length != 0) {

                ContentResolver trailerContentResolver = context.getContentResolver();

                trailerContentResolver.delete(MoviesContract.MoviesEntry.CONTENT_URI_TRAILER, null, null);

                trailerContentResolver.bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI_TRAILER, trailerContentValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getReviewInfo(Context context, String movieIdString, String path) {
        try {
            URL reviewUrl = NetworkUtils.getTrailerAndReviewUrl(movieIdString, path);

            String reviewJsonString = NetworkUtils.getResponseFromHttpUrl(reviewUrl);

            ContentValues[] reviewsContentValues = OpenMoviesJsonUtils.getReviewContentValuesFromJson(reviewJsonString);

            if (reviewsContentValues != null && reviewsContentValues.length != 0) {

                ContentResolver reviewsContentResolver = context.getContentResolver();

                reviewsContentResolver.delete(MoviesContract.MoviesEntry.CONTENT_URI_REVIEW, null, null);

                reviewsContentResolver.bulkInsert(MoviesContract.MoviesEntry.CONTENT_URI_REVIEW, reviewsContentValues);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
