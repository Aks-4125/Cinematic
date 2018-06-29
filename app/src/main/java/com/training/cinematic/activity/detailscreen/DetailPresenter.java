package com.training.cinematic.activity.detailscreen;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.training.cinematic.Model.MovieDetailModel;
import com.training.cinematic.Model.MovieGenre;
import com.training.cinematic.Model.SliderMovieImages;
import com.training.cinematic.Model.TvDetailModel;
import com.training.cinematic.Model.TvGenre;
import com.training.cinematic.R;
import com.training.cinematic.network.ApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public class DetailPresenter implements DetailController.IDetailPresenter {

    private Context context;
    private static final String TAG = DetailScreenActivity.class.getName();
    private ApiClient apiClient;
    Realm realm;
    Double rating;
    Uri uri;
    float rates;
    String convertedDate = "";
    DetailController.IDetailView detailView;
    SimpleDateFormat dateFormat;
    Date date1;
    int minutes, houres, time, seasonsofTv, episodes;
    RealmList<MovieGenre> genres;
    RealmList<TvGenre> tvGenres;
    StringBuffer sb;
    String language, overview, url, movieDate, movieName, tvName;
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
        detailView.ShowProgressbar();
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
                                setMovieData(movieDetailModel, context);
                                detailView.ShowMovieData();
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
                                setTvData(tvDetailModel, context);
                                detailView.ShowTvData();
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
            url = movieDetailModel.getHomepage();
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
            uri = Uri.parse(tvDetailModel.getHomepage());
            url = tvDetailModel.getHomepage();
        }

    }

    @Override
    public void setMovieData(MovieDetailModel movieDetailModel, Context context) {
       /* genres = movieDetailModel.getGenres();
        sb = new StringBuffer();
        for (int i = 0; i < genres.size(); i++) {
            sb.append(genres.get(i).getName());
            if (i != genres.size() - 1)
                sb.append(", ");
        }
        movieName = movieDetailModel.getTitle();
        url = movieDetailModel.getHomepage();
        rating = movieDetailModel.getVoteAverage();
        rates = (float) (rating / 2);
        movieDate = movieDetailModel.getReleaseDate();
        language = movieDetailModel.getOriginalLanguage();
        if (movieDetailModel.getOverview() != null) {
            overview = movieDetailModel.getOverview();
        } else {
            overview = "Not Avalible!";
        }
        if (movieDetailModel.getRuntime() != null) {
            time = movieDetailModel.getRuntime();
            houres = time / 60;
            minutes = time % 60;
        }
        if (movieDetailModel.getHomepage() != null) {
            url = movieDetailModel.getHomepage();
        } else {
            url = "Not Avalible!";
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            date1 = dateFormat.parse(movieDate);
            convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public void setTvData(TvDetailModel tvDetailModel, Context context) {
        /*tvGenres = tvDetailModel.getGenres();
        sb = new StringBuffer();
        for (int i = 0; i < tvGenres.size(); i++) {
            sb.append(tvGenres.get(i).getName());
            if (i != tvGenres.size() - 1)
                sb.append(", ");
        }
        tvName = tvDetailModel.getName();
        url = tvDetailModel.getHomepage();
        overview = tvDetailModel.getOverview();
        rating = tvDetailModel.getVoteAverage();
        rates = (float) (rating / 2);
        movieDate = tvDetailModel.getFirstAirDate();
        language = tvDetailModel.getOriginalLanguage();
        seasonsofTv = tvDetailModel.getNumberOfSeasons();
        episodes = tvDetailModel.getNumberOfEpisodes();
        if (tvDetailModel.getOverview() != null) {
            overview = tvDetailModel.getOverview();
        } else {
            overview = "Not Avalible!";
        }
        if (tvDetailModel.getHomepage() != null) {
            url = tvDetailModel.getHomepage();
        } else {
            url = "Not Avalible!";
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            date1 = dateFormat.parse(movieDate);
            convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
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
                            for (int i = 1; i < imagePath.size(); i++)
                                array.add(imagePath.get(i).getFilePath());
                            detailView.ShowImages();
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
                            for (int i = 1; i < imagePath.size(); i++)
                                array.add(imagePath.get(i).getFilePath());
                            detailView.ShowImages();
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