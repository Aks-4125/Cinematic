package com.training.cinematic;

import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.Model.TvModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dhruvisha on 5/31/2018.
 */

public interface ApiKeyForTvInterface {
    @GET("tv/popular")
    Call<TvModel> getTvList(@Query("api_key")String api_key);
    @GET("tv/{id}")
    Call<MovieModel>getTvDetails(@Path("id")int id,@Query("api_key") String api_key);

}
