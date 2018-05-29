package com.example.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.popularmovies.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rosha on 5/14/2018.
 */

public class OpenMoviesJsonUtils {

    /* Movies information. Each movie's info is an element of the "results" array */
    private static final String MOVIES_RESULTS = "results";

    private static final String MOVIE_TITLE = "title";

    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_BACKDROP_PATH = "backdrop_path";
    private static final String MOVIE_VOTE_AVERAGE = "vote_average";
    private static final String MOVIE_PLOT = "overview";


    public static ContentValues[] getMoviesContentValuesFromJson(Context context, String moviesJsonStr)
            throws JSONException {

        JSONObject movieResultsJson = new JSONObject(moviesJsonStr);

        JSONArray movieResultsJsonArray = movieResultsJson.getJSONArray(MOVIES_RESULTS);

        ContentValues[] movieContentValues = new ContentValues[movieResultsJsonArray.length()];


        for (int i = 0; i < movieResultsJsonArray.length(); i++) {

            String title;
            String releaseDate;
            String posterPath;
            String backdropPath;
            int vote_average;
            String plot;

            /* Get the JSON object representing the movie */
            JSONObject movieObject = movieResultsJsonArray.getJSONObject(i);

            title = movieObject.getString(MOVIE_TITLE);
            releaseDate = movieObject.getString(MOVIE_RELEASE_DATE);
            posterPath = movieObject.getString(MOVIE_POSTER_PATH);
            backdropPath = movieObject.getString(MOVIE_BACKDROP_PATH);
            vote_average = movieObject.getInt(MOVIE_VOTE_AVERAGE);
            plot = movieObject.getString(MOVIE_PLOT);

            ContentValues movieValues = new ContentValues();
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, title);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_BACKDROP_LINK, backdropPath);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_LINK, posterPath);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, vote_average);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS, plot);

            movieContentValues[i] = movieValues;
        }

        return movieContentValues;
    }
}
