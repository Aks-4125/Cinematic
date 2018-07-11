package com.training.cinematic.activity.detailscreen;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.training.cinematic.Model.MovieDetailModel;
import com.training.cinematic.Model.SliderMovieImages;
import com.training.cinematic.Model.TvDetailModel;
import com.training.cinematic.R;
import com.training.cinematic.network.ApiClient;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public class DetailPresenter implements DetailController.IDetailPresenter {

    private Context context;
    private static final String TAG = DetailPresenter.class.getName();
    private ApiClient apiClient;
    Realm realm;
    Double rating;
    float rates;
    DetailController.IDetailView detailView;
    String url, movieName, tvName;
    ArrayList<String> array = new ArrayList<String>();
    String[] image;

    public void setDetailView(DetailController.IDetailView detailView) {
        this.detailView = detailView;
    }

    public DetailPresenter(Context context) {
        this.context = context;
    }


    @Override
    public void storeAndFetchMoviesData(int movieDetailId, Context context) {
        detailView.showProgressbar();
        apiClient = new ApiClient(context);
        apiClient.getClient()
                .getMovieDetail((movieDetailId), context.getString(R.string.apikey))
                .enqueue(new Callback<MovieDetailModel>() {
                    @Override
                    public void onResponse(Call<MovieDetailModel> call, Response<MovieDetailModel> response) {
                        MovieDetailModel movieDetailModel = response.body();
                        if (movieDetailModel != null) {
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                realm1.copyToRealmOrUpdate(movieDetailModel);
                                detailView.setMovieData(movieDetailModel);
                            });
                        } else {
                            Toast.makeText(context, "please check internet", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieDetailModel> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
    }

    @Override
    public void storeAndFetchTvData(int id, Context context) {
        apiClient = new ApiClient(context);
        apiClient.getClient()
                .getTvDetail((id), context.getString(R.string.apikey))
                .enqueue(new Callback<TvDetailModel>() {
                    @Override
                    public void onResponse(Call<TvDetailModel> call, Response<TvDetailModel> response) {
                        TvDetailModel tvDetailModel = response.body();
                        if (tvDetailModel != null) {
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(realm1 -> {
                                realm1.insertOrUpdate(tvDetailModel);
                                detailView.setTvData(tvDetailModel);
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<TvDetailModel> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
    }

    @Override
    public void movieDataSharing(int movieID, Context context) {
        MovieDetailModel movieDetailModel = realm.where(MovieDetailModel.class)
                .equalTo("id", movieID).findFirst();
        if (movieDetailModel.getTitle() != null) {
            movieName = movieDetailModel.getTitle();
            rating = movieDetailModel.getVoteAverage();
            rates = (float) (rating / 2);
            if (movieDetailModel.getHomepage() == null) {
                url = "Not Available!";
            } else {
                url = movieDetailModel.getHomepage();

            }

        }
    }

    @Override
    public void tvDataSharing(int tvDetailId, Context context) {
        TvDetailModel tvDetailModel = realm.where(TvDetailModel.class)
                .equalTo("id", tvDetailId).findFirst();
        if (tvDetailModel.getName() != null) {
            tvName = tvDetailModel.getName();
            rating = tvDetailModel.getVoteAverage();
            rates = (float) (rating / 2);
            // uri = Uri.parse(tvDetailModel.getHomepage());
            if (tvDetailModel.getHomepage().isEmpty()) {
                url = "Not Available!";
            } else {
                url = tvDetailModel.getHomepage();

            }

        }

    }

    @Override
    public void fetchMovieImage(int movieDetailId, Context context) {
        apiClient.getClient()
                .getImages((movieDetailId), context.getString(Integer.parseInt((String.valueOf(R.string.apikey)))))
                .enqueue(new Callback<SliderMovieImages>() {
                    @Override
                    public void onResponse(Call<SliderMovieImages> call, Response<SliderMovieImages> response) {
                        List<SliderMovieImages.Backdrop> imagePath = response.body().getBackdrops();
                        if (imagePath != null) {
                            SliderMovieImages sliderImage = response.body();
                            Log.d("image path", "image path------->" + imagePath);
                            image = new String[imagePath.size()];
                            for (int i = 0; i < imagePath.size(); i++)
                                array.add(imagePath.get(i).getFilePath());
                            detailView.showImages();
                            Log.d("array", "arrya of images" + array);

                        }
                    }

                    @Override
                    public void onFailure(Call<SliderMovieImages> call, Throwable t) {
                        Log.e(TAG, t.toString());

                    }
                });
    }

    @Override
    public void fetchTvImages(int tvDetailId, Context context) {
        apiClient.getClient()
                .getTvImages((tvDetailId), context.getString(Integer.parseInt((String.valueOf(R.string.apikey)))))
                .enqueue(new Callback<SliderMovieImages>() {
                    @Override
                    public void onResponse(Call<SliderMovieImages> call, Response<SliderMovieImages> response) {
                        List<SliderMovieImages.Backdrop> imagePath = response.body().getBackdrops();
                        if (imagePath != null) {
                            SliderMovieImages sliderImage = response.body();
                            Log.d("image path", "image path------->" + imagePath);
                            image = new String[imagePath.size()];
                            for (int i = 0; i < imagePath.size(); i++)
                                array.add(imagePath.get(i).getFilePath());
                            detailView.showImages();
                            Log.d("array", "arrya of images" + array);

                        }
                    }

                    @Override
                    public void onFailure(Call<SliderMovieImages> call, Throwable t) {
                        Log.e(TAG, t.toString());

                    }
                });
    }
}