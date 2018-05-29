package com.example.popularmovies.data;

import android.content.Context;

/**
 * Created by rosha on 5/14/2018.
 */

public class MoviePreferences {

    private static final String DEFAULT_MOVIE_SORTING = "popular";
    private static final String TOP_RATED_SORTING = "top-rated";

    public static String getPopularMovies(Context context) {
        return getDefaultMovieSorting();
    }

    private static String getDefaultMovieSorting() {
        return  DEFAULT_MOVIE_SORTING;
    }

}
