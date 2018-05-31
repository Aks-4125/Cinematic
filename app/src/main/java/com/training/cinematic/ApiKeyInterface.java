package com.training.cinematic;

import com.training.cinematic.Model.MovieModel;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.training.cinematic.PopularMoviesRetorfil.KEY_POPULAR;

/**
 * Created by dhruvisha on 5/31/2018.
 */

public interface ApiKeyInterface {
  /*  @GET("movie/movie")
    Call<MovieModel> getmovies(@Query("api_key")String api_key);
    @GET("movie/{id}")
    Call<MovieModel> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);*/
    @GET("?key="+ KEY_POPULAR)
    Call<MovieModel>getMovielist(String keyPopular);
}
