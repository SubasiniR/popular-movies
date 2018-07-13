package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.sync.MoviesSyncUtils;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,
        MoviesAdapter.MoviesAdapterOnClickHandler {

    public static final String MOVIE_ID = "movieId";
    public static final String TITLE = "title";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER_LINK = "poster_link";
    public static final String VOTE = "vote";
    public static final String PLOT = "plot";

    public static final String DEFAULT_SORT_PREFERENCE = "popular";
    public static final String TOP_RATED_SORT_PREFERENCE = "top_rated";

    public static final String[] MAIN_MOVIE_POSTER_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_LINK,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_PLOT_SYNOPSIS
    };
    public static final int INDEX_MOVIE_ID = 0;
    public static final int INDEX_MOVIE_TITLE = 1;
    public static final int INDEX_MOVIE_RELEASE_DATE = 2;
    public static final int INDEX_MOVIE_POSTER_LINK = 3;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 4;
    public static final int INDEX_MOVIE_PLOT_SYNOPSIS = 5;


    private static final int MOVIES_LOADER_ID = 44;
    private static final int FAVORITE_MOVIES_LOADER_ID = 55;
    private static final String LIST_STATE_KEY = "list_state";
    Parcelable mListState;
    RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.rv_movies_thumbnail);
        int numberOfColumns = 2;
        mLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMoviesAdapter = new MoviesAdapter(this, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        if (isNetworkConnectionAvailable()) {
            getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
            MoviesSyncUtils.initialize(this, DEFAULT_SORT_PREFERENCE);
        } else {
            Toast.makeText(this, "Network not available. Check the internet connection.", Toast.LENGTH_LONG).show();
        }


    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

        mListState = mLayoutManager.onSaveInstanceState();
        state.putParcelable(LIST_STATE_KEY, mListState);
    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);

        if (state != null)
            mListState = state.getParcelable(LIST_STATE_KEY);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        Uri moviesQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;
        Uri moviesFavQueryUri = MoviesContract.MoviesEntry.CONTENT_URI_FAV;

        switch (loaderId) {

            case MOVIES_LOADER_ID:

                return new CursorLoader(this,
                        moviesQueryUri,
                        MAIN_MOVIE_POSTER_PROJECTION,
                        null,
                        null,
                        null);


            case FAVORITE_MOVIES_LOADER_ID:

                return new CursorLoader(this,
                        moviesFavQueryUri,
                        MAIN_MOVIE_POSTER_PROJECTION,
                        null,
                        null,
                        null);


            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        mMoviesAdapter.swapCursor(data);

        if (mListState != null) {
            mLayoutManager.onRestoreInstanceState(mListState);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_popular) {

            item.setChecked(true);

            MoviesSyncUtils.startImmediateSync(MainActivity.this, DEFAULT_SORT_PREFERENCE);

            return true;
        }
        if (id == R.id.action_top_rated) {

            item.setChecked(true);

            MoviesSyncUtils.startImmediateSync(MainActivity.this, TOP_RATED_SORT_PREFERENCE);

            return true;
        }

        if (id == R.id.action_favorite) {

            item.setChecked(true);

            getSupportLoaderManager().restartLoader(FAVORITE_MOVIES_LOADER_ID, null, this);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int movieId, String title, String date, String poster, String vote, String plot) {
        Intent intentToStartDetailsActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailsActivity.putExtra(MOVIE_ID, movieId);
        intentToStartDetailsActivity.putExtra(TITLE, title);
        intentToStartDetailsActivity.putExtra(RELEASE_DATE, date);
        intentToStartDetailsActivity.putExtra(POSTER_LINK, poster);
        intentToStartDetailsActivity.putExtra(VOTE, vote);
        intentToStartDetailsActivity.putExtra(PLOT, plot);

        startActivity(intentToStartDetailsActivity);
    }

    boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }
}
