package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.sync.MoviesSyncUtils;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>,
        MoviesAdapter.MoviesAdapterOnClickHandler {

    public static final String MOVIE_ID = "movieId";
    public static final String DEFAULT_SORT_PREFERENCE = "popular";
    public static final String TOP_RATED_SORT_PREFERENCE = "top_rated";
    public static final String[] MAIN_MOVIE_POSTER_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_LINK,
            MoviesContract.MoviesEntry._ID
    };
    public static final int INDEX_MOVIE_POSTER_LINK = 0;
    public static final int INDEX_MOVIE_ID = 1;
    private static final int MOVIES_LOADER_ID = 44;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mRecyclerView = findViewById(R.id.rv_movies_thumbnail);
        int numberOfColumns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mMoviesAdapter = new MoviesAdapter(this, this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        if (isNetworkConnectionAvailable()) {
            getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);

            MoviesSyncUtils.initialize(this, DEFAULT_SORT_PREFERENCE);
        } else {
            Toast.makeText(this, "Network not available. Check the internet connection.", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId) {

            case MOVIES_LOADER_ID:
                Uri moviesQueryUri = MoviesContract.MoviesEntry.CONTENT_URI;

                return new CursorLoader(this,
                        moviesQueryUri,
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
        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (data.getCount() != 0) {
            showMoviesDataView();
        }

    }

    private void showMoviesDataView() {
        mRecyclerView.setVisibility(View.VISIBLE);
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

        if(id == R.id.action_favorite){
            item.setChecked(true);
            //todo show only movies that are marked favorite here
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int id) {
        Intent intentToStartDetailsActivity = new Intent(MainActivity.this, DetailActivity.class);
        intentToStartDetailsActivity.putExtra(MOVIE_ID, id);
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
