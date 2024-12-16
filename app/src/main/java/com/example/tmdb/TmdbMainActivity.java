package com.example.tmdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.recycleviewmultipleviews.HomeActivity;
import com.example.recycleviewmultipleviews.MainActivity;
import com.example.recycleviewmultipleviews.R;
import com.example.recycleviewmultipleviews.databinding.ActivityTmdbMainBinding;
import com.example.tmdb.model.Tmdb;
import com.example.tmdb.model.TmdbMovie;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class TmdbMainActivity extends AppCompatActivity {
    static final String TAG = TmdbMainActivity.class.getSimpleName();
    static final String BASE_URL = "https://api.themoviedb.org/3/";
    static Retrofit retrofit = null;
    final static String API_KEY = "079ab243592a5951f8c22cff6482f258";
    private ActivityTmdbMainBinding binding;
    ProgressBar progressBar;
    PaginationAdapter movieAdapter;
    GridLayoutManager gridLayoutManager;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    List<TmdbMovie> tmdbMovieList= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // calling the action bar
        ActionBar actionBar = getSupportActionBar();
        /*ColorDrawable colorDrawable
                = new ColorDrawable(Color.parseColor("#ffffff"));

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);


        // showing the back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tmdb_main);
        //connect();
// we are initializing our adapter class and passing our arraylist to it.
        movieAdapter = new PaginationAdapter(this);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        gridLayoutManager = new GridLayoutManager(this, 2);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        binding.movieRecyclerView.setLayoutManager(gridLayoutManager);
        binding.movieRecyclerView.setItemAnimator(new DefaultItemAnimator());

        binding.setMyAdapter(movieAdapter);

        binding.movieRecyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        loadFirstPage();

    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<Tmdb> call = movieApiService.getMovies(API_KEY, currentPage);
        call.enqueue(new Callback<Tmdb>() {
            @Override
            public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {

                tmdbMovieList = response.body().results;
                Log.i(TAG, tmdbMovieList.toString());
                binding.mainProgress.setVisibility(View.GONE);
                movieAdapter.addAll(tmdbMovieList);

                if (currentPage <= TOTAL_PAGES) movieAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Tmdb> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });

    }

    private List<TmdbMovie> fetchResults(Response<Tmdb> response) {
        Tmdb topRatedMovies = response.body();
        return topRatedMovies.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);

        MovieApiService movieApiService = retrofit.create(MovieApiService.class);
        Call<Tmdb> call = movieApiService.getMovies(API_KEY, currentPage);
        call.enqueue(new Callback<Tmdb>() {
            @Override
            public void onResponse(Call<Tmdb> call, Response<Tmdb> response) {

                movieAdapter.removeLoadingFooter();
                isLoading = false;

                List<TmdbMovie> results = fetchResults(response);

                Log.i(TAG, results.toString());

                binding.mainProgress.setVisibility(View.GONE);
                movieAdapter.addAll(results);


                if (currentPage != TOTAL_PAGES) movieAdapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Tmdb> call, Throwable throwable) {
                Log.e(TAG, throwable.toString());
            }
        });
    }

}
