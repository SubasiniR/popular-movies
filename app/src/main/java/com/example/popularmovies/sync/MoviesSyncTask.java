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

            //TODO getUrl for the ids, reviews and trailers, retrive the json and pass it to openMoviesJsonUtils
//            Log.v(TAG, "Built URI " + jsonMoviesResponse);


            /* Parse the JSON into a list of movies content values */
            ContentValues[] moviesContentValues = OpenMoviesJsonUtils
                    .getMoviesContentValuesFromJson(context, jsonMoviesResponse);


            if (moviesContentValues != null && moviesContentValues.length != 0) {
                /* Get a handle on the ContentResolver to delete and insert data */
                ContentResolver moviesContentResolver = context.getContentResolver();

                moviesContentResolver.delete(
                        MoviesContract.MoviesEntry.CONTENT_URI,
                        null,
                        null);

                /* Insert our new data into movies's ContentProvider */
                moviesContentResolver.bulkInsert(
                        MoviesContract.MoviesEntry.CONTENT_URI,
                        moviesContentValues);


            }

        } catch (Exception e) {
            /* Server probably invalid */
            e.printStackTrace();
        }
    }
}
