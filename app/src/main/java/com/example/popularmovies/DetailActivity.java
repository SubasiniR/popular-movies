package com.example.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    final static String[] MOVIES_DETAIL_PROJECTION = {
            MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_BACKDROP_LINK,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_BACKDROP_PATH = 1;
    public static final int INDEX_MOVIE_TITLE = 2;
    public static final int INDEX_MOVIE_RELEASE_DATE = 3;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 4;
    public static final int INDEX_MOVIE_PLOT_SYNOPSIS = 5;

    private static final int ID_DETAIL_LOADER = 99;

//    private Uri mUri;
    private String mMovieId;

    private ImageView mBackdropIv;
    private TextView mTitleTv;
    private TextView mReleaseDateTv;
    private TextView mVoteAverageTv;
    private TextView mPlotSynopsisTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mBackdropIv =findViewById(R.id.iv_movie_backdrop);
        mTitleTv = findViewById(R.id.tv_movie_title);
        mReleaseDateTv = findViewById(R.id.tv_release_date);
        mVoteAverageTv = findViewById(R.id.tv_vote_average);
        mPlotSynopsisTv = findViewById(R.id.tv_plot);

//        mUri = getIntent().getData();
//        if (mUri == null) {
//            throw new NullPointerException("URI for DetailActivity cannot be null");
//        }

        Intent intent = getIntent();
        int id = intent.getExtras().getInt(MainActivity.MOVIE_ID);

        mMovieId = String.valueOf(id);

        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_DETAIL_LOADER:

                Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
                String selection = MoviesContract.MoviesEntry._ID + " = " + mMovieId;

                return new CursorLoader(this,
                        uri,
                        MOVIES_DETAIL_PROJECTION,
                        selection,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        boolean cursorHasValidData = false;
        if (data != null && data.moveToFirst()) {
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            Log.e("Details Cursor", "Data is invalid or empty");
            return;
        }

        String backdropPartPath = data.getString(INDEX_MOVIE_BACKDROP_PATH);
        String backdropUrl = null;
        if(backdropPartPath != null){
            backdropUrl = NetworkUtils.getImageUrl(backdropPartPath).toString();
        }
        Context context = mBackdropIv.getContext();
        Picasso.with(context).load(backdropUrl).placeholder(R.drawable.place_holder_image).error(R.drawable.error_image).into(mBackdropIv);

        String title = data.getString(INDEX_MOVIE_TITLE);
        mTitleTv.setText(title);

        String releaseDate = data.getString(INDEX_MOVIE_RELEASE_DATE);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedReleaseDate;
        if(date != null) {
            formattedReleaseDate = outputFormat.format(date);
        }else {
            formattedReleaseDate = "Information not available";
        }
        mReleaseDateTv.setText(formattedReleaseDate);

        String voteAvg = String.valueOf(data.getInt(INDEX_MOVIE_VOTE_AVERAGE)) + "/10";
        mVoteAverageTv.setText(voteAvg);

        String plot = data.getString(INDEX_MOVIE_PLOT_SYNOPSIS);
        mPlotSynopsisTv.setText(plot);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
