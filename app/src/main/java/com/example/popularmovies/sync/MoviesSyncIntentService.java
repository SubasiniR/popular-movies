package com.example.popularmovies.sync;

import android.app.IntentService;
import android.content.Intent;

import com.example.popularmovies.MainActivity;

/**
 * Created by rosha on 5/17/2018.
 */

public class MoviesSyncIntentService extends IntentService {

    public MoviesSyncIntentService() {
        super("MoviesSyncIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String sortPreference = intent.getStringExtra(MoviesSyncUtils.MOVIES_SORT_PREFERENCE);

        if (sortPreference == null) {
            sortPreference = MainActivity.DEFAULT_SORT_PREFERENCE;
        }
        MoviesSyncTask.syncMovies(this, sortPreference);

    }
}
