package com.training.cinematic.Fragment.poplarmovie;

import android.content.Context;
import android.util.Log;

import com.training.cinematic.Model.PoplarMovieModel;
import com.training.cinematic.Model.PopularMovieResult;
import com.training.cinematic.R;
import com.training.cinematic.network.ApiClient;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dhruvisha on 6/29/2018.
 */

public class PopularMoviePresenter implements PopularMovieController.IPopularMoviePresenter  {
    PopularMovieController.IPopularMovieView popularMovieView;
    private static final String TAG = PopularMoviePresenter.class.getName();
    private Realm realm;
    List<PopularMovieResult> movies;
    ApiClient apiClient;
    Context context;
    PoplarMovieModel movieModel;

    public void setPopularMovieView(PopularMovieController.IPopularMovieView popularMovieView) {
        this.popularMovieView = popularMovieView;
    }

    public PopularMoviePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void storeDataUsingRetrofit(Context context) {
        apiClient = new ApiClient(context);
        apiClient.getClient()
                .getMovielist(context.getString(R.string.apikey))
                .enqueue(new Callback<PoplarMovieModel>() {
                    @Override
                    public void onResponse(Call<PoplarMovieModel> call, Response<PoplarMovieModel> response) {
                        movieModel=response.body();
                        movies = response.body().getResults();
                        if (movies == null) {
                            popularMovieView.showProgressBar();

                        } else {
                            Log.d("popular movies", "popular movies size" + movies.size());
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realmm -> {
                                realmm.insertOrUpdate(movieModel);
                                realmm.insertOrUpdate(movies);
                                popularMovieView.setMovieData(movies);
                            });
                            popularMovieView.stopRefreshing();
                            popularMovieView.stopProgressBar();
                        }

                    }

                    @Override
                    public void onFailure(Call<PoplarMovieModel> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        popularMovieView.showProgressBar();
                    }
                });
    }

    @Override
    public void deleteData() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realmm -> {
            realmm.deleteAll();
        });
        storeDataUsingRetrofit(context);
    }


}