package com.example.popularmovies.utilities;

import android.content.ContentValues;
import android.content.Context;

import com.example.popularmovies.data.MoviesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by rosha on 5/14/2018.
 */

public class OpenMoviesJsonUtils {

    /* Movies information. Each movie's info is an element of the "results" array */
    private static final String MOVIES_RESULTS = "results";
    private static final String TRAILER_RESULTS = "results";
    private static final String REVIEW_RESULTS = "results";


    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_VOTE_AVERAGE = "vote_average";
    private static final String MOVIE_PLOT = "overview";

    private static final String TRAILER_ID = "key";
    private static final String TRAILER_NAME = "name";

    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";


    public static ContentValues[] getMoviesContentValuesFromJson(Context context, String moviesJsonStr)
            throws JSONException, IOException {

        JSONObject movieResultsJson = new JSONObject(moviesJsonStr);

        JSONArray movieResultsJsonArray = movieResultsJson.getJSONArray(MOVIES_RESULTS);

        ContentValues[] movieContentValues = new ContentValues[movieResultsJsonArray.length()];

        for (int i = 0; i < movieResultsJsonArray.length(); i++) {

            int movieId;
            String title;
            String releaseDate;
            String posterPath;
            String vote_average;
            String plot;

            /* Get the JSON object representing the movie */
            JSONObject movieObject = movieResultsJsonArray.getJSONObject(i);

            movieId = movieObject.getInt(MOVIE_ID);
            title = movieObject.getString(MOVIE_TITLE);
            releaseDate = movieObject.getString(MOVIE_RELEASE_DATE);
            posterPath = movieObject.getString(MOVIE_POSTER_PATH);
            vote_average = movieObject.getString(MOVIE_VOTE_AVERAGE);
            plot = movieObject.getString(MOVIE_PLOT);


            ContentValues movieValues = new ContentValues();
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, movieId);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, title);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE, releaseDate);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_LINK, posterPath);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE, vote_average);
            movieValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_PLOT_SYNOPSIS, plot);

            movieContentValues[i] = movieValues;
        }

        return movieContentValues;
    }

    public static ContentValues[] getTrailerContentValuesFromJson(String trailerJsonString) throws JSONException {

        JSONObject trailerResultsJsonObject = new JSONObject(trailerJsonString);

        JSONArray trailerJsonArray = trailerResultsJsonObject.getJSONArray(TRAILER_RESULTS);

        ContentValues[] trailerContentValues = new ContentValues[trailerJsonArray.length()];

        for(int i=0; i<trailerJsonArray.length() ; i++ ){
            String trailerId;
            String trailerName;

            JSONObject trailerJsonObject = trailerJsonArray.getJSONObject(i);

            trailerId = trailerJsonObject.getString(TRAILER_ID);
            trailerName = trailerJsonObject.getString(TRAILER_NAME);

            ContentValues trailerValues = new ContentValues();
            trailerValues.put(MoviesContract.MoviesEntry.COLUMN_TRAILER_ID, trailerId);
            trailerValues.put(MoviesContract.MoviesEntry.COLUMN_TRAILER_NAME, trailerName);

            trailerContentValues[i] = trailerValues;
        }

        return trailerContentValues;
    }

    public static ContentValues[] getReviewContentValuesFromJson(String reviewJsonString) throws JSONException {

        JSONObject reviewResultsJsonObject = new JSONObject(reviewJsonString);

        JSONArray reviewsJsonArray = reviewResultsJsonObject.getJSONArray(REVIEW_RESULTS);

        ContentValues[] reviewContentValues = new ContentValues[reviewsJsonArray.length()];

        for(int i=0; i< reviewsJsonArray.length(); i++){
            String reviewAuthor;
            String reviewContent;

            JSONObject reviewJsonObject = reviewsJsonArray.getJSONObject(i);

            reviewAuthor = reviewJsonObject.getString(REVIEW_AUTHOR);
            reviewContent = reviewJsonObject.getString(REVIEW_CONTENT);

            ContentValues reviewValues = new ContentValues();
            reviewValues.put(MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR, reviewAuthor);
            reviewValues.put(MoviesContract.MoviesEntry.COLUMN_REVIEW_CONTENT, reviewContent);

            reviewContentValues[i] = reviewValues;
        }

        return reviewContentValues;
    }
}
