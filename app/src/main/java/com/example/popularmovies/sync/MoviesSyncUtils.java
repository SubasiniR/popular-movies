package com.example.popularmovies.sync;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.popularmovies.data.MoviesContract;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * Created by rosha on 5/17/2018.
 */

public class MoviesSyncUtils {


    private static final int SYNC_INTERVAL_HOURS = 24;
    private static final int SYNC_INTERVAL_SECONDS = (int) TimeUnit.HOURS.toSeconds(SYNC_INTERVAL_HOURS);
    private static final int SYNC_FLEXTIME_SECONDS = SYNC_INTERVAL_SECONDS / 3;

    private static boolean sInitialized;

    private static final String MOVIES_SYNC_TAG = "movies-sync";
    public static final String MOVIES_SORT_PREFERENCE = "movies-sort-preference";

    /**
     * Schedules a repeating sync of movie's data using FirebaseJobDispatcher.
     * @param context Context used to create the GooglePlayDriver that powers the
     *                FirebaseJobDispatcher
     */
    static void scheduleFirebaseJobDispatcherSync(@NonNull final Context context, String sortPreference) {

        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Bundle bundle = new Bundle();
        bundle.putString(MOVIES_SORT_PREFERENCE, sortPreference);

        /* Create the Job to periodically sync movies*/
        Job syncMoviesJob = dispatcher.newJobBuilder()
                .setService(MoviesFirebaseJobService.class)
                .setTag(MOVIES_SYNC_TAG)
                .setConstraints(Constraint.DEVICE_IDLE)
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(
                        SYNC_INTERVAL_SECONDS,
                        SYNC_INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                .setReplaceCurrent(true)
                .setExtras(bundle)
                .build();

        /* Schedule the Job with the dispatcher */
        dispatcher.schedule(syncMoviesJob);
    }
    /**
     * Creates periodic sync tasks and checks to see if an immediate sync is required. If an
     * immediate sync is required, this method will take care of making sure that sync occurs.
     *
     * @param context Context that will be passed to other methods and used to access the
     *                ContentResolver
     */
    synchronized public static void initialize(@NonNull final Context context, final String sortPreference) {

        if (sInitialized) return;

        sInitialized = true;

        scheduleFirebaseJobDispatcherSync(context, sortPreference);

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {

                Uri moviesQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;

                String[] projectionColumns = {MoviesContract.MoviesEntry._ID};


                Cursor cursor = context.getContentResolver().query(
                        moviesQueryUri,
                        projectionColumns,
                        null,
                        null,
                        null);

                if (null == cursor || cursor.getCount() == 0) {
                    startImmediateSync(context, sortPreference);
                }
                cursor.close();
            }
        });

        checkForEmpty.start();
    }

    public static void startImmediateSync(@NonNull final Context context, String sortPreference) {
        Intent intentToSyncImmediately = new Intent(context, MoviesSyncIntentService.class);
        intentToSyncImmediately.putExtra(MOVIES_SORT_PREFERENCE, sortPreference);
        context.startService(intentToSyncImmediately);
    }
}
