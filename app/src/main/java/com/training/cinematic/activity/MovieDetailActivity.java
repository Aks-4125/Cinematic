package com.training.cinematic.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.training.cinematic.Adapter.MovieDetailAdapter;
import com.training.cinematic.ApiClient;
import com.training.cinematic.Model.MovieDetailModel;
import com.training.cinematic.Model.SliderMovieImages;
import com.training.cinematic.Model.TvDetailModel;
import com.training.cinematic.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends BaseActivity {
    private static final String TAG = MovieDetailActivity.class.getName();
    @BindView(R.id.viewpager1)
    ViewPager viewPager;
    @BindView(R.id.circleindicator)
    CircleIndicator indicator;
    private int currentpage = 0;
    private ArrayList<String> array = new ArrayList<String>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.text_name)
    TextView movieName;
    @BindView(R.id.text_description)
    TextView description;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.progrssbar)
    ProgressBar progressBar;
    TvDetailModel tvDetailModel;
    private ApiClient apiClient;
    private int movieDetailId;
    private int tvDetailId;
    int imagesId;
    int movieId;
    int tvId;
    int counter = 0;
    Realm realm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        collapsingToolbarLayout.setTitle("Movie");
        apiClient = new ApiClient(this);
        progressBar.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra("movieId")) {
            movieDetailId = getIntent().getIntExtra("movieId", 0);
            if (isConnected()) {
                movieDetail();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                getDataMovies(movieDetailId);
            }
        } else if (getIntent().hasExtra("tvId")) {
            tvDetailId = getIntent().getIntExtra("tvId", 0);
            Log.d("Id", "id for tv-->" + tvDetailId);
            if (isConnected()) {
                tvDetail();
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                getDataTv(tvDetailId);
            }
        }

    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void getDataMovies(int movieDetailId) {
        RealmResults<MovieDetailModel> realmResults = realm.where(MovieDetailModel.class).findAll();
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        for (MovieDetailModel movieDetailModel : realmResults) {

            movieId = movieDetailModel.getId();
            if (movieDetailId == movieId) {
                Double rates = movieDetailModel.getVoteAverage();
                float rating = (float) (rates / 2);
                ratingBar.setRating(rating);
                ratingBar.setVisibility(View.VISIBLE);
                movieName.setText(movieDetailModel.getTitle());
                description.setText(movieDetailModel.getOverview());

            }

        }


    }

    public void getDataTv(int tvDetailId ){
        RealmResults<TvDetailModel> tvDetailModelRealmResults=realm.where(TvDetailModel.class).findAll();
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        for (TvDetailModel tvDetailModel:tvDetailModelRealmResults){
            tvId=tvDetailModel.getId();
            if (tvId==tvDetailId){
                Double rate=tvDetailModel.getVoteAverage();
                float rating=(float) (rate/2);
                ratingBar.setRating(rating);
                ratingBar.setVisibility(View.VISIBLE);
                movieName.setText(tvDetailModel.getName());
                description.setText(tvDetailModel.getOverview());
            }

        }



    }

    public void tvDetail() {
        apiClient.getClient()
                .getTvDetail((tvDetailId), getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                .enqueue(new Callback<TvDetailModel>() {
                    @Override
                    public void onResponse(Call<TvDetailModel> call, Response<TvDetailModel> response) {

                        tvDetailModel = response.body();
                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(realm1 -> {
                            realm1.insertOrUpdate(tvDetailModel);
                        });
                        getDataMovies(tvDetailId);

                        tvId = tvDetailModel.getId();

                        String name = tvDetailModel.getName();
                        String descriptionTv = tvDetailModel.getOverview();
                        Double rate = tvDetailModel.getVoteAverage();
                        float rating = (float) (rate / 2);
                        ratingBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        ratingBar.setRating(rating);
                        ratingBar.setVisibility(View.VISIBLE);
                        movieName.setText(name);
                        description.setText(descriptionTv);
                        viewPager.setVisibility(View.VISIBLE);
                        indicator.setVisibility(View.VISIBLE);


                    }

                    @Override
                    public void onFailure(Call<TvDetailModel> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
        apiClient.getClient()
                .getTvImages((tvDetailId), getString(Integer.parseInt((String.valueOf(R.string.apikey)))))
                .enqueue(new Callback<SliderMovieImages>() {
                    @Override
                    public void onResponse(Call<SliderMovieImages> call, Response<SliderMovieImages> response) {
                        List<SliderMovieImages.Backdrop> imagePath = response.body().getBackdrops();
                        if (response.body().getBackdrops() != null) {
                            SliderMovieImages sliderImage = response.body();
                            imagesId = sliderImage.getId();


                            Log.d("image path", "image path------->" + imagePath);
                            String[] image = new String[imagePath.size()];
                            for (int i = 1; i < imagePath.size(); i++)
                                if (tvDetailId == imagesId) {

                                    array.add(imagePath.get(i).getFilePath());
                                }
                            Log.d("image id", "image id for the movieDetail detail page" + imagesId);
                            Log.d("moviedetails", "tvDetailId" + imagesId);
                            Log.d("array", "arrya of images" + array);

                            viewPager.setAdapter(new MovieDetailAdapter(MovieDetailActivity.this, array));


                            final Handler handeer = new Handler();
                            final Runnable run = new Runnable() {
                                @Override
                                public void run() {
                                    if (currentpage == image.length) {
                                        currentpage = 0;
                                    }
                                    viewPager.setCurrentItem(currentpage++, true);
                                    progressBar.setVisibility(View.GONE);
                                    indicator.setViewPager(viewPager);
                                }
                            };

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handeer.post(run);
                                }
                            }, 2500, 2500);
                        }




                    }

                    @Override
                    public void onFailure(Call<SliderMovieImages> call, Throwable t) {
                        Log.e(TAG, t.toString());

                    }
                });
    }

 /*   public void getDetailOfTv() {
        apiClient.getClient()
                .getTvDetail((tvDetailId), getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                .enqueue(new Callback<TvDetailModel>() {
                    @Override
                    public void onResponse(Call<TvDetailModel> call, Response<TvDetailModel> response) {
                        progressBar.setVisibility(View.GONE);

                        tvDetailModel = response.body();

                        tvId = tvDetailModel.getId();

                        String name = tvDetailModel.getName();
                        String descriptionTv = tvDetailModel.getOverview();
                        Double rate = tvDetailModel.getVoteAverage();
                        float rating = (float) (rate / 2);

                        ratingBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                        ratingBar.setRating(rating);
                        movieName.setText(name);
                        description.setText(descriptionTv);
                       *//* realm = Realm.getDefaultInstance();
                        realm.executeTransaction(realm1 -> {
                            realm1.insertOrUpdate( tvDetailModel);
                        });*//*
                        viewPager.setVisibility(View.VISIBLE);
                        //  ratingBar.setVisibility(View.VISIBLE);
                        indicator.setVisibility(View.VISIBLE);
                      *//*  movieName.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);*//*
                        //progressBar.setVisibility(View.GONE);


                    }

                    @Override
                    public void onFailure(Call<TvDetailModel> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });
    }*/

    public void movieDetail() {
        apiClient.getClient()
                .getMovieDetail((movieDetailId), getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                .enqueue(new Callback<MovieDetailModel>() {
                    @Override
                    public void onResponse(Call<MovieDetailModel> call, Response<MovieDetailModel> response) {
                        MovieDetailModel movieDetailModel = response.body();

                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(realm1 -> {
                            realm1.insertOrUpdate(movieDetailModel);
                        });
                        getDataMovies(movieDetailId);

                    }

                    @Override
                    public void onFailure(Call<MovieDetailModel> call, Throwable t) {
                        Log.e(TAG, t.toString());

                        //close progress

                    }
                });
        apiClient.getClient()
                .getImages((movieDetailId), getString(Integer.parseInt((String.valueOf(R.string.apikey)))))
                .enqueue(new Callback<SliderMovieImages>() {
                    @Override
                    public void onResponse(Call<SliderMovieImages> call, Response<SliderMovieImages> response) {


                        List<SliderMovieImages.Backdrop> imagePath = response.body().getBackdrops();
                        if (imagePath != null) {


                            SliderMovieImages sliderImage = response.body();
                            imagesId = sliderImage.getId();


                            Log.d("image path", "image path------->" + imagePath);
                            String[] image = new String[imagePath.size()];
                            for (int i = 1; i < imagePath.size(); i++)
                                array.add(imagePath.get(i).getFilePath());

                            Log.d("image id", "image id for the movieDetail detail page" + imagesId);
                            Log.d("moviedetails", "moviedetailid" + imagesId);
                            Log.d("array", "arrya of images" + array);
                            viewPager.setAdapter(new MovieDetailAdapter(MovieDetailActivity.this, array));
                            indicator.setViewPager(viewPager);


                            // getDetailOfMovie();
                            final Handler handeer = new Handler();
                            final Runnable run = new Runnable() {
                                @Override
                                public void run() {
                                    if (currentpage == image.length) {
                                        currentpage = 0;
                                    }
                                    viewPager.setCurrentItem(currentpage++, true);
                                    progressBar.setVisibility(View.GONE);
                                }
                            };
                            viewPager.setVisibility(View.VISIBLE);
                            indicator.setVisibility(View.VISIBLE);
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handeer.post(run);
                                }
                            }, 2500, 2500);
                        }




                    }

                    @Override
                    public void onFailure(Call<SliderMovieImages> call, Throwable t) {
                        Log.e(TAG, t.toString());
                        //  getDetailOfMovie();

                    }
                });

    }

  /*  public void getDetailOfMovie() {
        apiClient.getClient()
                .getMovieDetail((movieDetailId), getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                .enqueue(new Callback<MovieDetailModel>() {
                    @Override
                    public void onResponse(Call<MovieDetailModel> call, Response<MovieDetailModel> response) {
                        MovieDetailModel movieDetailModel = response.body();

                        realm = Realm.getDefaultInstance();
                        realm.executeTransaction(realm1 -> {
                            realm1.insertOrUpdate(movieDetailModel);
                        });
                        getDataMovies(movieDetailId);
                              *//*  Double rates=movieDetailModel.getVoteAverage();
                                float rating=(float) (rates/2);
                                ratingBar.setRating(rating);
                                movieName.setText(movieDetailModel.getTitle());
                                description.setText(movieDetailModel.getOverview());
                               // progressBar.setVisibility(View.GONE);
                                ratingBar.setVisibility(View.VISIBLE);
                                movieName.setVisibility(View.VISIBLE);
                                description.setVisibility(View.VISIBLE);





                        movieId = movieDetailModel.getId();*//*
                     *//*   String name = movieDetailModel.getTitle();
                        String descriptionMovie = movieDetailModel.getOverview();
                        Double rating = movieDetailModel.getVoteAverage();
                        Log.d("rating", "ratings-->" + rating);
                        float rate = (float) (rating / 2);
                        movieName.setText(name);
                        description.setText(descriptionMovie);*//*
                     *//*   ratingBar.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                return true;
                            }
                        });
                     //   ratingBar.setRating(rate);

                        viewPager.setVisibility(View.VISIBLE);*//*
                    *//*    ratingBar.setVisibility(View.VISIBLE);
                        indicator.setVisibility(View.VISIBLE);
                        movieName.setVisibility(View.VISIBLE);
                        description.setVisibility(View.VISIBLE);*//*
                        //progressBar.setVisibility(View.GONE);
                        //visible view
                        //close progress

                    }

                    @Override
                    public void onFailure(Call<MovieDetailModel> call, Throwable t) {
                        Log.e(TAG, t.toString());

                        //close progress

                    }
                });

    }*/
}
