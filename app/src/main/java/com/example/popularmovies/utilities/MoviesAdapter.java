package com.example.popularmovies.utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by rosha on 5/8/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder>{

    private String[] mImageString = new String[0];
    LayoutInflater mLayoutInflator;

    public MoviesAdapter(){

    }

    public MoviesAdapter(Context context, String[] imageString){
        this.mImageString = imageString;
        this.mLayoutInflator = LayoutInflater.from(context);

    }

    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForMoviesGrid = R.layout.movies_grid_item;
        mLayoutInflator = LayoutInflater.from(context);
        View view = mLayoutInflator.inflate(layoutIdForMoviesGrid,viewGroup,false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder moviesAdapterViewHolder, int position) {

        //TODO : bind the image to the ImageView(mMoviesThumbnailIV)
        String imageString = mImageString[position];
        Context context = moviesAdapterViewHolder.mMovieThumbnailIV.getContext();
        Picasso.with(context).load(imageString).into(moviesAdapterViewHolder.mMovieThumbnailIV);

    }

    @Override
    public int getItemCount() {
        if(null == mImageString) {
            return 0;
        }
        return mImageString.length;
    }

    public void setMoviesThumbnail(String[] imageString) {
        mImageString = imageString;
        notifyDataSetChanged();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder{

        public final ImageView mMovieThumbnailIV;

        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieThumbnailIV = itemView.findViewById(R.id.iv_movie_thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //TODO : add functions on click.
                }
            });
        }
    }

}
