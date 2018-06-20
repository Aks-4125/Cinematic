package com.training.cinematic.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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

public class DetailScreenActivity extends BaseActivity {
    private static final String TAG = DetailScreenActivity.class.getName();
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.circleindicator)
    CircleIndicator indicator;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.text_date)
    TextView date;
    @BindView(R.id.rating)
    RatingBar ratingBar;
    @BindView(R.id.text_description)
    TextView description;
    @BindView(R.id.toolbar1)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.progrssbar)
    ProgressBar progressBar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appBarLayout;

    int currentpage = 0;
    boolean appBarExpanded;
    Menu collapsedMenu;
    ArrayList<String> array = new ArrayList<String>();
    Realm realm;
    private ApiClient apiClient;
    private int movieDetailId;
    private int tvDetailId;
    TvDetailModel tvDetailModel;
    int movieId;
    int tvId;
    int imagesId;
    String name;
    ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_screen);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        progressBar.setVisibility(View.VISIBLE);
        apiClient = new ApiClient(this);
        setSupportActionBar(toolbar);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //  Vertical offset == 0 indicates appBar is fully  expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false;
                    invalidateOptionsMenu();
                } else {
                    appBarExpanded = true;
                    invalidateOptionsMenu();
                }
            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
            ratingBar.setOnTouchListener((v, event) -> true);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareData();
                }
            });
            if (getIntent().hasExtra("movieId")) {
                movieDetailId = getIntent().getIntExtra("movieId", 0);
                if (isConnected()) {
                    movieDeatils();
                } else {
                    Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    getMovieData(movieDetailId);
                }
            } else if (getIntent().hasExtra("tvId")) {
                tvDetailId = getIntent().getIntExtra("tvId", 0);
                if (isConnected()) {
                    tvDetails();
                } else {
                    Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    getTvData(tvDetailId);
                }
            }



    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if ((!appBarExpanded || menu.size() != 0)) {
            //collapsed
            menu.add("Share")
                    .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            shareData();
                            return true;
                        }
                    })
                    .setIcon(R.drawable.ic_share_black_24dp)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        } else {
            //expanded
        }

        return super.onPrepareOptionsMenu(menu);
    }
    private void shareData(){
        RealmResults<MovieDetailModel> realmResults = realm.where(MovieDetailModel.class).findAll();
        for (MovieDetailModel movieDetailModel : realmResults) {

            movieId = movieDetailModel.getId();

            if (movieDetailId == movieId) {
                String name = movieDetailModel.getTitle();
                String url = movieDetailModel.getHomepage();
                Uri movieuri = Uri.parse(movieDetailModel.getHomepage());
                Double rating = movieDetailModel.getVoteAverage();
                float rates = (float) (rating / 2);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Cinematic App");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Movie Name:\n" + name +
                        "\n URL: \n" + movieuri + "\n Rating: " + rates);
                intent.putExtra(Intent.EXTRA_STREAM, url);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Share link"));
            }
        }
        RealmResults<TvDetailModel> tvDetailModelRealmResults = realm.where(TvDetailModel.class).findAll();
        for (TvDetailModel tvDetailModel : tvDetailModelRealmResults) {
            tvId = tvDetailModel.getId();
            if (tvId == tvDetailId) {
                String name = tvDetailModel.getName();
                Double rating = tvDetailModel.getVoteAverage();
                Uri tvurl = Uri.parse(tvDetailModel.getHomepage());
                float rates = (float) (rating / 2);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Cinematic App");
                intent.putExtra(Intent.EXTRA_TEXT, "Tv Show Name:" + name + "\n URL:"
                        + tvurl + "\n Rating:" + rates);
                startActivity(Intent.createChooser(intent, "Share link"));

            }
        }

    }


    private void getMovieData(int movieId){
        MovieDetailModel movieDetailModel = realm.where(MovieDetailModel.class)
                .equalTo("id", movieDetailId).findFirst();

        if (movieDetailModel != null) {
            Double rates = movieDetailModel.getVoteAverage();
            float rating = (float) (rates / 2);
            ratingBar.setRating(rating);
            ratingBar.setVisibility(View.VISIBLE);
            name=movieDetailModel.getTitle();
            String movieDate = movieDetailModel.getReleaseDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date1;
            String convertedDate = "";
            try {
                date1 = dateFormat.parse(movieDate);
                convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date.setText(convertedDate);
           // movieName.setText(movieDetailModel.getTitle());
            description.setText(movieDetailModel.getOverview());
            /*if (getSupportActionBar() != null)*/
                getSupportActionBar().setTitle(name);
                collapsingToolbarLayout.setTitle(name);
        }

       /*
        RealmResults<MovieDetailModel> realmResults = realm.where(MovieDetailModel.class).findAll();

        for (MovieDetailModel movieDetailModel : realmResults) {

            movieId = movieDetailModel.getId();
            if (movieDetailId == movieId) {


            }
*/
        //}

    }


    private void getTvData(int tvId){
        TvDetailModel tvDetailModel = realm.where(TvDetailModel.class)
                .equalTo("id", tvDetailId).findFirst();
        Double rate = tvDetailModel.getVoteAverage();
        float rating = (float) (rate / 2);
        ratingBar.setRating(rating);
        ratingBar.setVisibility(View.VISIBLE);
       // movieName.setText(tvDetailModel.getName());
        name=tvDetailModel.getName();
        description.setText(tvDetailModel.getOverview());
        String tvDate = tvDetailModel.getFirstAirDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date1;
        String convertedDate = "";
        try {
            date1 = dateFormat.parse(tvDate);
            convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setText(convertedDate);
            getSupportActionBar().setTitle(name);
            collapsingToolbarLayout.setTitle(name);
    /*    RealmResults<TvDetailModel> tvDetailModelRealmResults = realm.where(TvDetailModel.class).findAll();
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        for (TvDetailModel tvDetailModel : tvDetailModelRealmResults) {
            tvId = tvDetailModel.getId();
            if (tvId == tvDetailId) {

            }

        }*/

    }

    private void movieDeatils(){
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
                        getMovieData(movieDetailId);

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
                            viewPager.setAdapter(new MovieDetailAdapter(DetailScreenActivity.this, array));
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
                                    viewPager.setVisibility(View.VISIBLE);
                                    progressBar.setVisibility(View.GONE);
                                }
                            };

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

    private void tvDetails(){
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
                        getTvData(tvDetailId);


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

                            viewPager.setAdapter(new MovieDetailAdapter(DetailScreenActivity.this, array));


                            final Handler handeer = new Handler();
                            final Runnable run = new Runnable() {
                                @Override
                                public void run() {
                                    if (currentpage == image.length) {
                                        currentpage = 0;
                                    }
                                    viewPager.setCurrentItem(currentpage++, true);
                                    viewPager.setVisibility(View.VISIBLE);
                                    indicator.setVisibility(View.VISIBLE);
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


}
