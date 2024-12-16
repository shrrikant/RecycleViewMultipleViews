package com.example.tmdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.bumptech.glide.Glide;
import com.example.app.DentistDataModal;
import com.example.recycleviewmultipleviews.HomeActivity;
import com.example.recycleviewmultipleviews.R;
import com.example.recycleviewmultipleviews.databinding.ActivityMovieBinding;
import com.example.tmdb.model.Genre;
import com.example.tmdb.model.Tmdb;
import com.example.tmdb.model.TmdbMovie;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailsActivity extends AppCompatActivity {
    private ActivityMovieBinding binding;
    static final String TAG = TmdbMainActivity.class.getSimpleName();
    static final String BASE_URL = "https://api.themoviedb.org/3/";
    static Retrofit retrofit = null;
    final static String API_KEY = "079ab243592a5951f8c22cff6482f258";
    int movie_id = 0;
    List<TmdbMovie> tmdbMovieList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie);
        //setContentView(R.layout.activity_movie_details);

        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        /*ColorDrawable colorDrawable
                = new ColorDrawable(Color.WHITE);

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);*/
        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie_id = extras.getInt("movie_id", 0);
            //The key argument here must match that used in the other activity
        }

        connect();
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), TmdbMainActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void connect() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<TmdbMovie> call = movieApiService.getMovieById(movie_id, API_KEY);
        call.enqueue(new Callback<TmdbMovie>() {
            @Override
            public void onResponse(Call<TmdbMovie> call, Response<TmdbMovie> response) {

                if(null != response) {

                    TmdbMovie tmdbMovie = new TmdbMovie();
                    tmdbMovie = response.body();
                    Log.i(TAG, "onResponse: i" +tmdbMovie.toString());
                    binding.setModel(tmdbMovie);

                    String image = "";
                    image = "https://image.tmdb.org/t/p/w500" + tmdbMovie.getPoster_path();
                    Glide.with(getApplicationContext()).load(image).into(binding.poster);

                    String genres="";
                    List<Genre> genreArrayList = new ArrayList<>();
                    genreArrayList = tmdbMovie.getGenres();
                    for (Genre i : genreArrayList) {
                        genres += i.name +", ";
                    }

                    binding.genres.setText(genres.substring(0, genres.length() - 2));
                }
                //Log.i(TAG, tmdbMovieList.toString());


            }

            @Override
            public void onFailure(Call<TmdbMovie> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }


}