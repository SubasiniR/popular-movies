package com.example.popularmovies.sync;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.popularmovies.MainActivity;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by rosha on 5/17/2018.
 */

public class MoviesFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchMoviesTask;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {

        mFetchMoviesTask = new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
                Bundle bundle = jobParameters.getExtras();
                String sortPreference = bundle.get(MoviesSyncUtils.MOVIES_SORT_PREFERENCE).toString();
                if (sortPreference == null) {
                    sortPreference = MainActivity.DEFAULT_SORT_PREFERENCE;
                }
                MoviesSyncTask.syncMovies(context, sortPreference);
                jobFinished(jobParameters, false);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                jobFinished(jobParameters, false);
            }
        };

        mFetchMoviesTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mFetchMoviesTask != null) {
            mFetchMoviesTask.cancel(true);
        }
        return true;
    }
}
