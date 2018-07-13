package com.example.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.sync.MoviesSyncTask;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements
        TrailersAdapter.TrailersAdapterOnClickHandler,
        LoaderCallbacks<Cursor> {


    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_MOVIE_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_TITLE = 3;
    public static final int INDEX_MOVIE_RELEASE_DATE = 4;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 5;
    public static final int INDEX_MOVIE_PLOT_SYNOPSIS = 6;

    final static String[] MOVIES_DETAIL_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_LINK,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_PLOT_SYNOPSIS
          };

    public static final int INDEX_TRAILER_ID = 0;
    public static final int INDEX_TRAILER_NAME = 1;

    final static String[] TRAILER_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_TRAILER_ID,
            MoviesContract.MoviesEntry.COLUMN_TRAILER_NAME
    };

    public static final int INDEX_REVIEW_AUTHOR = 0;
    public static final int INDEX_REVIEW_CONTENT = 1;
    final static String[] REVIEW_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_REVIEW_AUTHOR,
            MoviesContract.MoviesEntry.COLUMN_REVIEW_CONTENT
    };

    private static final int TRAILER_LOADER_ID = 11;
    private static final int REVIEW_LOADER_ID = 33;

    private static final String TRAILER_PATH = "videos";
    private static final String REVIEWS_PATH = "reviews";

    private int mPosition = RecyclerView.NO_POSITION;


    @BindView(R.id.tv_movie_title_label)
    TextView mTitleTV;
    @BindView(R.id.iv_movie_poster)
    ImageView mPosterIV;
    @BindView(R.id.tv_release_year)
    TextView mReleaseYearTV;
    @BindView(R.id.tv_vote_average)
    TextView mVoteAverageTV;
    @BindView(R.id.fav_Button)
    ToggleButton mFavoriteBtn;
    @BindView(R.id.tv_plot)
    TextView mPlotTV;
    @BindView(R.id.trailer_rv)
    RecyclerView mTrailerRV;
    @BindView(R.id.review_rv)
    RecyclerView mReviewRV;

    TrailersAdapter mTrailersAdapter;
    ReviewsAdapter mReviewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        final int movieId = intent.getExtras().getInt(MainActivity.MOVIE_ID);
        final String title = intent.getStringExtra(MainActivity.TITLE);
        final String releaseDate = intent.getStringExtra(MainActivity.RELEASE_DATE);
        final String posterLink = intent.getStringExtra(MainActivity.POSTER_LINK);
        final String vote = intent.getStringExtra(MainActivity.VOTE);
        final String plot = intent.getStringExtra(MainActivity.PLOT);

        final String movieIdString = String.valueOf(movieId);

        mTitleTV.setText(title);

        String posterUrl = null;
        if (posterLink != null) {
            posterUrl = NetworkUtils.getImageUrl(posterLink).toString();
        }
        Picasso.with(this).load(posterUrl).placeholder(R.drawable.place_holder_image).error(R.drawable.error_image).into(mPosterIV);

        mReleaseYearTV.setText("Released on: \n" + FormattedDate(releaseDate));

        mVoteAverageTV.setText("User Rating: \n" + vote + "/10");

        mPlotTV.setText(plot);

        if (MovieExistsInFav(movieIdString)) {
            mFavoriteBtn.setChecked(true);
            mFavoriteBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_star_yellow));
        } else {
            mFavoriteBtn.setChecked(false);
            mFavoriteBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_star_grey));
        }

        mFavoriteBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    ContentValues favContentValue = new ContentValues();
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_ID, movieId);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_TITLE, title);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_RELEASE_DATE, releaseDate);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_POSTER_LINK, posterLink);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_VOTE_AVERAGE, vote);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_PLOT_SYNOPSIS, plot);

                    Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI_FAV, favContentValue);
                    if (uri != null) {
                        mFavoriteBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_star_yellow));
                        Toast.makeText(getBaseContext(), "Added to your Favorites List", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    mFavoriteBtn.setChecked(false);
                    mFavoriteBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_star_grey));
                    String selection = MoviesContract.MoviesEntry.COLUMN_FAV_ID + " = ? ";
                    String[] selectionArgs = {movieIdString};
                    getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI_FAV, selection, selectionArgs);
                }

            }
        });


        Thread getInfoFromNetworkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                MoviesSyncTask.getTrailerInfo(DetailActivity.this, movieIdString, TRAILER_PATH);

                MoviesSyncTask.getReviewInfo(DetailActivity.this, movieIdString, REVIEWS_PATH);

            }
        });


        mTrailerRV.setLayoutManager(new LinearLayoutManager(this));
        mTrailerRV.setHasFixedSize(true);
        mTrailersAdapter = new TrailersAdapter(this, this);
        mTrailerRV.setAdapter(mTrailersAdapter);

        mReviewRV.setLayoutManager(new LinearLayoutManager(this));
        mReviewRV.setHasFixedSize(true);
        mReviewsAdapter = new ReviewsAdapter(this);
        mReviewRV.setAdapter(mReviewsAdapter);

        getSupportLoaderManager().initLoader(TRAILER_LOADER_ID, null, DetailActivity.this);
        getSupportLoaderManager().initLoader(REVIEW_LOADER_ID, null, DetailActivity.this);

        getInfoFromNetworkThread.start();

    }

    public Boolean MovieExistsInFav(String movieId) {


        String favUri = String.valueOf(MoviesContract.MoviesEntry.CONTENT_URI_FAV);

        Uri uri = Uri.parse(favUri).buildUpon().appendPath(movieId).build();

        Cursor cursor = getContentResolver().query(uri,
                MOVIES_DETAIL_PROJECTION,
                null,
                null,
                null);

        return cursor.getCount() == 1;
    }

    public String FormattedDate(String releaseDate) {
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date = null;
        try {
            date = inputFormat.parse(releaseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedReleaseDate;
        if (date != null) {
            formattedReleaseDate = outputFormat.format(date);
        } else {
            formattedReleaseDate = "Information not available";
        }

        return formattedReleaseDate;
    }

    @Override
    public void onClick(Context context, String trailerId) {

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerId));

        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerId));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        Uri trailerContentUri = MoviesContract.MoviesEntry.CONTENT_URI_TRAILER;
        Uri reviewContentUri = MoviesContract.MoviesEntry.CONTENT_URI_REVIEW;

        switch (loaderId){

            case TRAILER_LOADER_ID:

                return new CursorLoader(this,
                        trailerContentUri,
                        TRAILER_PROJECTION,
                        null,
                        null,
                        null);

            case REVIEW_LOADER_ID:

                return new CursorLoader(this,
                        reviewContentUri,
                        REVIEW_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);

        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {

            case TRAILER_LOADER_ID: {
                mTrailersAdapter.swapCursor(data);
                if (mPosition == RecyclerView.NO_POSITION) {
                    mPosition = 0;
                }
                mTrailerRV.smoothScrollToPosition(mPosition);
                if(data.getCount() != 0){
                    showTrailerDataView();
                }
                break;
            }
            case REVIEW_LOADER_ID: {
                mReviewsAdapter.swapCursor(data);
                if (mPosition == RecyclerView.NO_POSITION) {
                    mPosition = 0;
                }
                mReviewRV.smoothScrollToPosition(mPosition);
                if(data.getCount() != 0){
                    showReviewDataView();
                }
                break;
            }
        }
    }

    private void showTrailerDataView(){
        mTrailerRV.setVisibility(View.VISIBLE);
    }
    private void showReviewDataView() {
        mReviewRV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
