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
        }
        if (getIntent().hasExtra("tvId")) {
            movieDetailId = getIntent().getIntExtra("tvId", 0);
        }
        if (getIntent().hasExtra("upComingMovieId")) {
            movieDetailId = getIntent().getIntExtra("upComingMovieId", 0);
        }

        apiClient.getClient()
                .getImages((movieDetailId), getString(Integer.parseInt((String.valueOf(R.string.apikey)))))
                .enqueue(new Callback<SliderMovieImages>() {
                    @Override
                    public void onResponse(Call<SliderMovieImages> call, Response<SliderMovieImages> response) {

                        List<SliderMovieImages.Backdrop> imagePath = response.body().getBackdrops();
                        Log.d("image path", "image path------->" + imagePath);
                        SliderMovieImages sliderImage = response.body();
                        Integer imagesId = sliderImage.getId();


                        String[] image = new String[imagePath.size()];
                        for (int j = 0; j < image.length; j++) {

                            image[j] = imagePath.get(j).getFilePath();
                        }
                        for (int i = 0; i < image.length; i++)
                            if (movieDetailId == sliderImage.getId()) {
                                array.add(image[i]);
                            }

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
                        }, 2000, 2000);
                    }


                    @Override
                    public void onFailure(Call<SliderMovieImages> call, Throwable t) {
                        Log.e(TAG, t.toString());
                    }
                });


    }
}
