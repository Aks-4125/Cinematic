package com.training.cinematic;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dhruvisha on 5/30/2018.
 */

public class PopularMoviesRetorfil {
    private static final String BASE_URL ="https://api.themoviedb.org/3/movie/popular?/";
    public static final String KEY_POPULAR ="fec13c5a0623fefac5055a3f7b 823553";
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

}
