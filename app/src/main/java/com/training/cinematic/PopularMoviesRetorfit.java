package com.training.cinematic;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dhruvisha on 5/30/2018.
 */

public class PopularMoviesRetorfit {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;

    public static Retrofit getdata() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
