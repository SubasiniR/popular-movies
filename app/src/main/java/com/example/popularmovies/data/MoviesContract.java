package com.example.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rosha on 5/16/2018.
 */

public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.example.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final String PATH_MOVIES_FAV = "favorites";

    public static final String PATH_TRAILERS = "trailers";

    public static final String PATH_REVIEWS = "reviews";


    /* Inner class that defines the table contents of the movies table */
    public static final class MoviesEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final Uri CONTENT_URI_FAV = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES_FAV)
                .build();

        public static final Uri CONTENT_URI_TRAILER = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILERS)
                .build();
        public static final Uri CONTENT_URI_REVIEW =BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();


        //TABLE NAMES
        public static final String TABLE_NAME = "movies_list";

        public static final String TABLE_NAME_FAV = "fav_list";

        public static final String TABLE_NAME_TRAILER = "trailer_info";

        public static final String TABLE_NAME_REVIEW = "review_info";

        //COLUMN NAMES FOR MOVIE_LIST TABLE
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_POSTER_LINK = "movie_poster";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_MOVIE_PLOT_SYNOPSIS = "plot";

        //COLUMN NAMES FOR FAV_LIST TABLE
        public static final String COLUMN_FAV_ID = "movie_id";
        public static final String COLUMN_FAV_TITLE = "title";
        public static final String COLUMN_FAV_RELEASE_DATE = "release_date";
        public static final String COLUMN_FAV_POSTER_LINK = "movie_poster";
        public static final String COLUMN_FAV_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_FAV_PLOT_SYNOPSIS = "plot";

        //COLUMN NAMES FOR TRAILER_INFO TABLE
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_TRAILER_NAME = "trailer_name";

        //COLUMN NAMES FOR REVIEW_INFO TABLE
        public static final String COLUMN_REVIEW_AUTHOR = "review_author";
        public static final String COLUMN_REVIEW_CONTENT = "review_content";
    }

}
