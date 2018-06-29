package com.training.cinematic.Fragment.populartv;

import android.content.Context;
import android.util.Log;

import com.training.cinematic.Model.TvModel;
import com.training.cinematic.Model.TvResult;
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

public class PopularTvPresenter implements PopularTvController.IPopularTvPresenter {
    private static final String TAG = PopularTvPresenter.class.getName();
    PopularTvController.IPopularTvView popularTvView;
    Context context;
    private Realm realm;
    private ApiClient apiClient;
    List<TvResult> popularTv;
    TvModel tvModel;

    public void setPopularTvView(PopularTvController.IPopularTvView popularTvView) {
        this.popularTvView = popularTvView;
    }

    public PopularTvPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void storeDataUsingRetrofit(Context context) {
        apiClient = new ApiClient(context);
        apiClient.getClient()
                .getTvList(context.getString(R.string.apikey))
                .enqueue(new Callback<TvModel>() {
                    @Override
                    public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                        tvModel = response.body();
                        popularTv = response.body().getResults();
                        if (popularTv == null) {
                            popularTvView.showProgressBar();
                        } else {
                            Log.d("TAG", "Popular Tv list--->" + popularTv.size());
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                realm1.insertOrUpdate(popularTv);
                                realm1.insertOrUpdate(tvModel);
                                popularTvView.setTvData(popularTv);
                            });
                            popularTvView.stopProgressBar();
                            popularTvView.stopRefreshing();

                        }

                    }

                    @Override
                    public void onFailure(Call<TvModel> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        popularTvView.stopProgressBar();
                    }

                });

    }

    @Override
    public void deleteData() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            realm1.delete(TvModel.class);
            realm1.delete(TvResult.class);
        });
        storeDataUsingRetrofit(context);
    }
}
