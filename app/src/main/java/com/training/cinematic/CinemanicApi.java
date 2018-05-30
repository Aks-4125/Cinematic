package com.training.cinematic;

import com.training.cinematic.Model.MovieModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by dhruvisha on 5/30/2018.
 */

public class CinemanicApi {
    private static final String KEY="fec13c5a0623fefac5055a3f7b 823553 ";
    private static final String BASRURL="https://api.themoviedb.org/3/movie/popular?";

    public static PostService postService=null;
    public static PostService getservice(){
        if (postService==null){
            //create
            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(BASRURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            postService=retrofit.create(PostService.class);
        }return postService;
    }

    public interface PostService{
        @GET("?key="+KEY)
        Call<MovieModel>getMovielist();

    }
}
