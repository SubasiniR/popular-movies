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


    /* Inner class that defines the table contents of the movies table */
    public static final class MoviesEntry implements BaseColumns {

        /* The base CONTENT_URI used to query the Movies table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies_list";


        //COLUMNS

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_MOVIE_BACKDROP_LINK = "movie_backdrop";

        public static final String COLUMN_MOVIE_POSTER_LINK = "movie_poster";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_PLOT_SYNOPSIS = "plot";



    }

}
