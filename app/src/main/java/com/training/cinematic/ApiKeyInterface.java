package com.training.cinematic;

import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.Model.SliderMovieImages;
import com.training.cinematic.Model.TvModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dhruvisha on 6/4/2018.
 */

public interface ApiKeyInterface {
    @GET("tv/popular")
    Call<TvModel> getTvList(@Query("api_key")String api_key);
    @GET("tv/{id}")
    Call<TvModel>getTvDetails(@Path("id")int id, @Query("api_key") String api_key);

    @GET("movieDetail/popular")
    Call<MovieModel> getMovielist(@Query("api_key") String api_key);

    @GET("movieDetail/{id}")
    Call<MovieModel> getMovieDetails(@Path("id") int id,@Query("api_key") String api_key);

    @GET("movieDetail/{id}/images")
    Call<SliderMovieImages> getImages(@Path("id") int id,@Query("api_key") String api_key);

    @GET("tv/{id}/images")
    Call<SliderMovieImages> getTvImages(@Path("id") int id,@Query("api_key") String api_key);

    @GET("movieDetail/{id}/images")
    Call<SliderMovieImages> getId(@Path("id") int id);


}
