package com.example.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by rosha on 7/3/2018.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailersAdapterViewHolder> {

    private Context mContext;

    private final TrailersAdapterOnClickHandler mClickHandler;

    private Cursor mCursor;

    public interface TrailersAdapterOnClickHandler{
        void onClick(Context context, String trailerId);
    }


     public TrailersAdapter(@NonNull Context context, TrailersAdapterOnClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;

    }

    @Override
    public TrailersAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int trailerLayout = R.layout.trailer_items;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(trailerLayout, viewGroup, false);

        return new TrailersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailersAdapterViewHolder trailersAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);
        String trailerName = mCursor.getString(DetailActivity.INDEX_TRAILER_NAME);
        trailersAdapterViewHolder.mTrailerNameTv.setText(trailerName);

    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class TrailersAdapterViewHolder extends ViewHolder implements View.OnClickListener {

         final TextView mTrailerNameTv;

         public TrailersAdapterViewHolder(View itemView) {

            super(itemView);

            mTrailerNameTv = itemView.findViewById(R.id.trailer_name_tv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
             int adapterPosition = getAdapterPosition();
             mCursor.moveToPosition(adapterPosition);
             String trailerId = mCursor.getString(DetailActivity.INDEX_TRAILER_ID);

             mClickHandler.onClick(mContext, trailerId);

        }
    }
}
