package com.training.cinematic.network;

import com.training.cinematic.Model.MovieDetailModel;
import com.training.cinematic.Model.PoplarMovieModel;
import com.training.cinematic.Model.SliderMovieImages;
import com.training.cinematic.Model.TvDetailModel;
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
    Call<TvModel> getTvList(@Query("api_key") String api_key);
    @GET("tv/{id}")
    Call<TvModel> getTvDetails(@Path("id") int id, @Query("api_key") String api_key);

    @GET("movie/popular")
    Call<PoplarMovieModel> getMovielist(@Query("api_key") String api_key);

    @GET("movie/{id}/images")
    Call<SliderMovieImages> getImages(@Path("id") int id, @Query("api_key") String api_key);

    @GET("tv/{id}/images")
    Call<SliderMovieImages> getTvImages(@Path("id") int id, @Query("api_key") String api_key);

    @GET("movie/{id}")
    Call<MovieDetailModel> getMovieDetail(@Path("id")int id,@Query("api_key") String api_key);

    @GET("tv/{id}")
    Call<TvDetailModel> getTvDetail(@Path("id")int id, @Query("api_key") String api_key);


}
