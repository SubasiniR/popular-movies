package com.example.popularmovies.utilities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.example.popularmovies.R;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private ImageView mImageView;

    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Todo update the data from online and remove the hardcoded string
        String[] imageString = {"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg "};

        mRecyclerView = findViewById(R.id.rv_movies_thumbnail);
        int numberOfColumns = 2;
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mMoviesAdapter = new MoviesAdapter(this, imageString);
        mRecyclerView.setAdapter(mMoviesAdapter);


    }
}
