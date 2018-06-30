package com.training.cinematic.Fragment.upcomingmovie;

import com.training.cinematic.Model.MovieResult;

import java.util.List;

/**
 * Created by dhruvisha on 6/29/2018.
 */

public interface UpComingMovieController {

    interface IUpComingMoviePresenter {
        class GetJSONFromURL {
        }

        void callJsonClass(String uri);

        void deleteData();

    }

    interface IUpComingMovieView {
        void showProgressBar();

        void stopProgressBar();

        void startReshreshing();

        void stopRefreshing();

        void setOfflineData();

        void setMovieData(List<MovieResult> movieModels);
    }
}
