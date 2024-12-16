package com.example.tmdb;

import com.example.tmdb.model.Tmdb;
import com.example.tmdb.model.TmdbMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {
    @GET("movie/popular")
    Call<Tmdb> getMovies(@Query("api_key") String apiKey, @Query("page") Integer page);

    @GET("movie/{id}")
    Call<TmdbMovie> getMovieById(@Path("id") int id, @Query("api_key") String apiKey);
}
