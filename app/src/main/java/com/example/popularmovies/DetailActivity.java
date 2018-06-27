package com.example.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.popularmovies.data.MoviesContract;
import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    final static String[] MOVIES_DETAIL_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_MOVIE_ID,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_POSTER_LINK,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_RELEASE_DATE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            MoviesContract.MoviesEntry.COLUMN_MOVIE_PLOT_SYNOPSIS
    };

    public static final int INDEX_ID = 0;
    public static final int INDEX_MOVIE_ID = 1;
    public static final int INDEX_MOVIE_POSTER_PATH = 2;
    public static final int INDEX_MOVIE_TITLE = 3;
    public static final int INDEX_MOVIE_RELEASE_DATE = 4;
    public static final int INDEX_MOVIE_VOTE_AVERAGE = 5;
    public static final int INDEX_MOVIE_PLOT_SYNOPSIS = 6;

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

        String movieIdString = String.valueOf(movieId);

        mTitleTV.setText(title);

        String posterUrl = null;
        if (posterLink != null) {
            posterUrl = NetworkUtils.getImageUrl(posterLink).toString();
        }
        Picasso.with(this).load(posterUrl).placeholder(R.drawable.place_holder_image).error(R.drawable.error_image).into(mPosterIV);

        mReleaseYearTV.setText(FormattedDate(releaseDate));

        mVoteAverageTV.setText(vote);

        mPlotTV.setText(plot);

        if(MovieExistsInFav(movieIdString)) {
            mFavoriteBtn.setChecked(true);
            mFavoriteBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_star_yellow));
        }else {
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
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_RELEASE_DATE,releaseDate);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_POSTER_LINK, posterLink);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_VOTE_AVERAGE, vote);
                    favContentValue.put(MoviesContract.MoviesEntry.COLUMN_FAV_PLOT_SYNOPSIS, plot);

                    Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI_FAV, favContentValue);
                    if(uri != null){
                        mFavoriteBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_star_yellow));
                        Toast.makeText(getBaseContext(), "Added to your Favorites List", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(getBaseContext(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    mFavoriteBtn.setChecked(false);
                    mFavoriteBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.img_star_grey));
                    String selection = MoviesContract.MoviesEntry.COLUMN_FAV_ID + " = ? ";
                    String[] selectionArgs = {String.valueOf(movieId)};
                    getContentResolver().delete(MoviesContract.MoviesEntry.CONTENT_URI_FAV, selection, selectionArgs);
                }

            }
        });

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

    public String FormattedDate(String releaseDate){
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
}
