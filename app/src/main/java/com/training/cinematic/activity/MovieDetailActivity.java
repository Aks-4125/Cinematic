package com.training.cinematic.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.training.cinematic.Adapter.MovieDetailAdapter;
import com.training.cinematic.ApiClient;
import com.training.cinematic.Model.SliderMovieImages;
import com.training.cinematic.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {
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

    private ApiClient apiClient;
    private int movieDetailId;
    private int tvDetailId;

    int imagesId;
    int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        collapsingToolbarLayout.setTitle("Movie");
        apiClient = new ApiClient(this);

        if (getIntent().hasExtra("movieId")) {
            movieDetailId = getIntent().getIntExtra("movieId", 0);
            movieDetail();
        }
        if (getIntent().hasExtra("tvId")) {
            tvDetailId = getIntent().getIntExtra("tvId", 0);
            Log.d("Id", "id for tv-->" + tvDetailId);
            tvDetail();
        }
        if (getIntent().hasExtra("upComingMovieId")) {
            movieDetailId = getIntent().getIntExtra("upComingMovieId", 0);
            movieDetail();
        }


    }

    public void tvDetail() {
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
                            indicator.setViewPager(viewPager);
                            final Handler handeer = new Handler();
                            final Runnable run = new Runnable() {
                                @Override
                                public void run() {
                                    if (currentpage == image.length) {
                                        currentpage = 0;
                                    }
                                    viewPager.setCurrentItem(currentpage++, true);
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

    public void movieDetail() {
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
                                if (movieDetailId == imagesId) {

                                    array.add(imagePath.get(i).getFilePath());
                                }
                            Log.d("image id", "image id for the movieDetail detail page" + imagesId);
                            Log.d("moviedetails", "moviedetailid" + imagesId);
                            Log.d("array", "arrya of images" + array);
                            viewPager.setAdapter(new MovieDetailAdapter(MovieDetailActivity.this, array));
                            indicator.setViewPager(viewPager);
                            final Handler handeer = new Handler();
                            final Runnable run = new Runnable() {
                                @Override
                                public void run() {
                                    if (currentpage == image.length) {
                                        currentpage = 0;
                                    }
                                    viewPager.setCurrentItem(currentpage++, true);
                                }
                            };

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    handeer.post(run);
                                }
                            }, 2500, 2500);
                        } else {
                            tvDetail();
                        }
                    }

                    @Override
                    public void onFailure(Call<SliderMovieImages> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });


    }
}
