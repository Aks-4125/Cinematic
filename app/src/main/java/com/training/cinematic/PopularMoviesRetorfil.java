package com.training.cinematic;

import com.training.cinematic.Model.MovieModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dhruvisha on 5/30/2018.
 */

public class PopularMoviesRetorfil {
    private static final String BASE_URL ="https://api.themoviedb.org/3/movie/popular?/";
    private static final String KEY="fec13c5a0623fefac5055a3f7b 823553";
    private static Retrofit retrofit;

    public static Retrofit getdata()
    {
        if (retrofit!=null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
return retrofit;
    }
    public interface ApiKeyInterface{
        @GET("movie/movie")
        Call<MovieModel>getmovies(@Query("api_key")String api_key);
        @GET("movie/{id}")
        Call<MovieModel> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);
    }
}
