package com.example.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by rosha on 5/8/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

//    private String[] mImageString = new String[0];
    LayoutInflater mLayoutInflater;
    private final Context mContext;

    private final MoviesAdapterOnClickHandler mClickHandler;

    public interface MoviesAdapterOnClickHandler{
        void onClick(int id);
    }

    private Cursor mCursor;

//
//    public MoviesAdapter(){
//    }

    public MoviesAdapter(@NonNull Context context, MoviesAdapterOnClickHandler clickHandler){
        mContext = context;

        mClickHandler = clickHandler;

    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForMoviesGrid = R.layout.movies_grid_item;
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(layoutIdForMoviesGrid,viewGroup,false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);

        String imagePartString = mCursor.getString(MainActivity.INDEX_MOVIE_POSTER_LINK);
        String imageUrl = null;
        if(imagePartString != null){
            imageUrl = NetworkUtils.getImageUrl(imagePartString).toString();
        }
        Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.place_holder_image).error(R.drawable.error_image).into(moviesAdapterViewHolder.mMovieThumbnailIV);

    }

    @Override
    public int getItemCount() {
        if(null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

//    public void setMoviesThumbnail(String[] imageString) {
//        mImageString = imageString;
//        notifyDataSetChanged();
//    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class MoviesAdapterViewHolder extends ViewHolder implements View.OnClickListener{

        final ImageView mMovieThumbnailIV;

        MoviesAdapterViewHolder(View itemView) {
            super(itemView);

            mMovieThumbnailIV = itemView.findViewById(R.id.iv_movie_thumbnail);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int id = mCursor.getInt(MainActivity.INDEX_MOVIE_ID);
            mClickHandler.onClick(id);
        }
    }
}
