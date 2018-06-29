package com.training.cinematic.activity.detailscreen;

import android.content.Context;

import com.training.cinematic.Model.MovieDetailModel;
import com.training.cinematic.Model.TvDetailModel;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public interface DetailController {
    interface IDetailPresenter {
        void storeAndFetchMoviesData(int movieDetailId, Context context);

        void storeAndFetchTvData(int id, Context context);

        void movieDataSharing(int movieDetailId, Context context);

        void tvDataSharing(int tvDetailId, Context context);

        void fetchMovieImage(int movieDetailId, Context context);

        void fetchTvImages(int tvDetailId, Context context);

    }

    interface IDetailView {

        void ShowProgressbar();

        void StopProgressbar();

        void ShowImages();

        void setMovieData(MovieDetailModel movieDetailModel);

        void setTvData(TvDetailModel tvDetailModel);


    }

}
