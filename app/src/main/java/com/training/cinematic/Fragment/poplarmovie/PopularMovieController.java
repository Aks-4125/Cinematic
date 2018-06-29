package com.training.cinematic.Fragment.poplarmovie;

import android.content.Context;

import com.training.cinematic.Model.PopularMovieResult;

import java.util.List;

/**
 * Created by dhruvisha on 6/29/2018.
 */

public interface PopularMovieController {

    interface IPopularMoviePresenter {
        void storeDataUsingRetrofit(Context context);

        void deleteData();

    }

    interface IPopularMovieView {
        void showProgressBar();

        void stopProgressBar();

        void startReshreshing();

        void stopRefreshing();

        void setOfflineData();

        void setMovieData(List<PopularMovieResult> movies);
    }
}
