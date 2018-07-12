package com.example.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by rosha on 5/14/2018.
 */

public final class NetworkUtils {

    final static String API_KEY_STRING = "api_key";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String STATIC_MOVIES_URL =
            "http://api.themoviedb.org/3/movie";
    private static final String STATIC_IMAGE_URL =
            "http://image.tmdb.org/t/p/w185/";

    private static final String MOVIES_BASE_URL = STATIC_MOVIES_URL;
    private static final String IMAGE_BASE_URL = STATIC_IMAGE_URL;


    //TODO Add the API KEY below. Get the key from themoviedb.org
    private static String API_KEY = BuildConfig.ApiKey;


    public static URL getUrl(Context context, String sortPreference) {

        String sortBy = sortPreference;
        return buildUrlForMovies(sortBy);

    }

    public static URL getImageUrl(String imagePartUrl){

        return buildImageUrl(imagePartUrl);

    }

    public static URL getTrailerAndReviewUrl(String movieId, String path) {
        return buildTrailerAndReviewUrl(movieId, path);
    }

    private static URL buildTrailerAndReviewUrl(String movieId, String path) {

        Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                .buildUpon()
                .appendPath(movieId)
                .appendPath(path)
                .appendQueryParameter(API_KEY_STRING, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    public static URL buildImageUrl(String imagePartUrl){
        Uri builtUri = Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendEncodedPath(imagePartUrl)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlForMovies(String sortPreference) {
        Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(sortPreference)
                .appendQueryParameter(API_KEY_STRING, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
           InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
