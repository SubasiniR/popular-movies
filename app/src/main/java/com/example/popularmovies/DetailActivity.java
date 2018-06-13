package com.example.popularmovies;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    final static String[] MOVIES_DETAIL_PROJECTION = {
            MoviesContract.MoviesEntry._ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_LINK,
            MoviesContract.MoviesEntry.COLUMN_TITLE,
            MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_PLOT_SYNOPSIS
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_POSTER_PATH = 1;
    public static final int INDEX_MOVIE_TITLE = 2;
    public static final int INDEX_MOVIE_RELEASE_DATE = 3;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 4;
    public static final int INDEX_MOVIE_PLOT_SYNOPSIS = 5;

    private static final int ID_DETAIL_LOADER = 99;

//    private Uri mUri;
    private String mMovieId;

    @BindView(R.id.tv_movie_title_label) TextView mTitleTV;
    @BindView(R.id.iv_movie_poster) ImageView mPosterIV;
    @BindView(R.id.tv_release_year) TextView mReleaseYearTV;
//    @BindView(R.id.tv_movie_total_time) TextView mTotalTimeTV;
    @BindView(R.id.tv_vote_average) TextView mVoteAverageTV;
    @BindView(R.id.fav_button) TextView mFavoriteBtn;
    @BindView(R.id.tv_plot) TextView mPlotTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

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

        String posterPartPath = data.getString(INDEX_MOVIE_POSTER_PATH);
        String posterUrl = null;
        if(posterPartPath != null){
            posterUrl = NetworkUtils.getImageUrl(posterPartPath).toString();
        }

        Picasso.with(this).load(posterUrl).placeholder(R.drawable.place_holder_image).error(R.drawable.error_image).into(mPosterIV);

        String title = data.getString(INDEX_MOVIE_TITLE);
        mTitleTV.setText(title);

        String releaseDate = data.getString(INDEX_MOVIE_RELEASE_DATE);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("yyyy");
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
        mReleaseYearTV.setText(formattedReleaseDate);

        String voteAvg = data.getString(INDEX_MOVIE_VOTE_AVERAGE) + "/10";
        mVoteAverageTV.setText(voteAvg);

        String plot = data.getString(INDEX_MOVIE_PLOT_SYNOPSIS);
        mPlotTV.setText(plot);

        //TEMP data
//        mTotalTimeTV.setText("120mins");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
