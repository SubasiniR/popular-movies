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
 * Created by rosha on 7/11/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    ReviewsAdapter(@NonNull Context context){
        mContext = context;
    }

    @Override
    public ReviewsAdapter.ReviewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int reviewLayout = R.layout.reviews_item;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(reviewLayout, viewGroup, false);

        return new ReviewsAdapter.ReviewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewsAdapter.ReviewsAdapterViewHolder reviewsAdapterViewHolder, int position) {

        mCursor.moveToPosition(position);
        String reviewAuthor = mCursor.getString(DetailActivity.INDEX_REVIEW_AUTHOR) + " says :";
        String reviewContent = mCursor.getString(DetailActivity.INDEX_REVIEW_CONTENT);
        reviewsAdapterViewHolder.mReviewAuthorTv.setText(reviewAuthor);
        reviewsAdapterViewHolder.mReviewContentTv.setText(reviewContent);
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

    public class ReviewsAdapterViewHolder extends ViewHolder{

        final TextView mReviewAuthorTv;
        final TextView mReviewContentTv;

        ReviewsAdapterViewHolder(View itemView) {

            super(itemView);

            mReviewAuthorTv = itemView.findViewById(R.id.reviewAuthorTv);
            mReviewContentTv = itemView.findViewById(R.id.reviewContentTv);

        }
    }
}
