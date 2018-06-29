package com.training.cinematic.Fragment.populartv;

import android.content.Context;

import com.training.cinematic.Model.TvResult;

import java.util.List;

/**
 * Created by dhruvisha on 6/29/2018.
 */

public interface PopularTvController {

    interface IPopularTvPresenter {
        void storeDataUsingRetrofit(Context context);

        void deleteData();

    }

    interface IPopularTvView {
        void showProgressBar();

        void stopProgressBar();

        void startReshreshing();

        void stopRefreshing();

        void setOfflineData();

        void setTvData(List<TvResult> tvResults);
    }
}
