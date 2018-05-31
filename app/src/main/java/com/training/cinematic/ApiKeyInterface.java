package com.training.cinematic;

import com.training.cinematic.Model.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dhruvisha on 5/31/2018.
 */

public interface ApiKeyInterface {
    @GET("movie/popular")
    Call<MovieModel> getMovielist(@Query("api_key") String api_key);

    @GET("movie/{id}")
    Call<MovieModel> getMovieDetails(@Path("id") int id,@Query("api_key") String api_key);
}
