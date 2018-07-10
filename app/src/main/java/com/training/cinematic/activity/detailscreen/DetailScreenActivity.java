package com.training.cinematic.activity.detailscreen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.training.cinematic.Adapter.DetailScreenAdapter;
import com.training.cinematic.Model.MovieDetailModel;
import com.training.cinematic.Model.MovieGenre;
import com.training.cinematic.Model.TvDetailModel;
import com.training.cinematic.Model.TvGenre;
import com.training.cinematic.R;
import com.training.cinematic.Utils.Utils;
import com.training.cinematic.activity.BaseActivity;
import com.training.cinematic.activity.CategoryEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmList;
import me.relex.circleindicator.CircleIndicator;

import static com.training.cinematic.Utils.ConstantHelper.CAT_NAME;

public class DetailScreenActivity extends BaseActivity implements DetailController.IDetailView {
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
    @BindView(R.id.desciption)
    TextView description;
    @BindView(R.id.toolbar1)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.progrssbar)
    ProgressBar progressBar;
    @BindView(R.id.appbarlayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.language)
    TextView language;
    @BindView(R.id.txt_category)
    TextView category;
    @BindView(R.id.duration)
    TextView duration;
    @BindView(R.id.seasons)
    TextView season;
    @BindView(R.id.homepage)
    TextView homepage;
    @BindView(R.id.text_duration)
    TextView textDuration;
    @BindView(R.id.text_season)
    TextView textSeason;
    @BindView(R.id.text_episodes)
    TextView textofEpisode;
    @BindView(R.id.episodes)
    TextView episodesTv;
    int currentpage = 0;
    boolean appBarExpanded;
    Realm realm;
    private int movieDetailId;
    private int tvDetailId;
    DetailPresenter detailPresenter;
    int catId = 0;
    SimpleDateFormat dateFormat;
    Date date1;
    int minutes, houres, time, seasonsofTv, episodes;
    RealmList<MovieGenre> genres;
    RealmList<TvGenre> tvGenres;
    StringBuffer sb;
    String cast_language, overview, url, movieDate, movieName, tvName;
    Double rating;
    float rates;
    String convertedDate = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_detail_screen);
        ButterKnife.bind(this);
        detailPresenter = new DetailPresenter(this);
        detailPresenter.setDetailView(this);
        realm = Realm.getDefaultInstance();
        showProgressbar();
        setSupportActionBar(toolbar);
        catId = getIntent().getIntExtra(CAT_NAME, 0);
        description.setMovementMethod(new ScrollingMovementMethod());
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
                stopProgressbar();

            }
        } else if (getIntent().hasExtra("tvId")) {
            tvDetailId = getIntent().getIntExtra("tvId", 0);
            if (isConnected()) {
                tvDetails();
            } else {
                Toast.makeText(this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                stopProgressbar();
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

    private void movieDeatils() {
        detailPresenter.storeAndFetchMoviesData(movieDetailId, DetailScreenActivity.this);
        detailPresenter.fetchMovieImage(movieDetailId, DetailScreenActivity.this);
    }


    private void tvDetails() {
        detailPresenter.storeAndFetchTvData(tvDetailId, DetailScreenActivity.this);
        detailPresenter.fetchTvImages(tvDetailId, DetailScreenActivity.this);

    }

    public void shareData() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Cinematic App");
        if (catId == CategoryEnum.category.MOVIES.getValue()) {
            detailPresenter.movieDataSharing(movieDetailId, DetailScreenActivity.this);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.putExtra(Intent.EXTRA_TEXT, "Movie Name:\n" + detailPresenter.movieName + "\n URL:"
                        + Html.fromHtml(detailPresenter.url, Html.FROM_HTML_MODE_LEGACY) + "\n Rating:" + detailPresenter.rates);
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, "Movie Name:\n" + detailPresenter.movieName + "\n URL:"
                        + Html.fromHtml(detailPresenter.url) + "\n Rating:" + detailPresenter.rates);
            }
        } else if (catId == CategoryEnum.category.TV.getValue()) {
            detailPresenter.tvDataSharing(tvDetailId, DetailScreenActivity.this);
            if (Build.VERSION.SDK_INT >= 24) {
                intent.putExtra(Intent.EXTRA_TEXT, "Tv Show Name:" + detailPresenter.tvName + "\n URL:"
                        + Html.fromHtml(detailPresenter.url, Html.FROM_HTML_MODE_LEGACY) + "\n Rating:" + detailPresenter.rates);
            } else {
                intent.putExtra(Intent.EXTRA_TEXT, "Tv Show Name:" + detailPresenter.tvName + "\n URL:"
                        + Html.fromHtml(detailPresenter.url) + "\n Rating:" + detailPresenter.rates);
            }
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Share link"));
    }


    @Override
    public void showImages() {
        viewPager.setAdapter(new DetailScreenAdapter(DetailScreenActivity.this, detailPresenter.array, fab));
        indicator.setViewPager(viewPager);
        final Handler handeer = new Handler();
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                if (currentpage == detailPresenter.image.length) {
                    currentpage = 0;
                }
                viewPager.setCurrentItem(currentpage++, true);
                viewPager.setVisibility(View.VISIBLE);
                stopProgressbar();
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

    @Override
    public void setMovieData(MovieDetailModel movieDetailModel) {
        genres = movieDetailModel.getGenres();
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
        cast_language = movieDetailModel.getOriginalLanguage();
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
        }


        //show movie data
        category.setText(sb);
        ratingBar.setRating(rates);
        duration.setText(houres + " Hour and " + minutes + " Minutes");
        date.setText(convertedDate);
        language.setText(cast_language);
        description.setText(overview);
        if (url != null) {
            if (Build.VERSION.SDK_INT >= 24) {
                homepage.setText(Html.fromHtml(url, Html.FROM_HTML_MODE_LEGACY));
            } else {
                homepage.setText(Html.fromHtml(url));
            }
        }
        getSupportActionBar().setTitle(movieName);
        collapsingToolbarLayout.setTitle(movieName);
        duration.setVisibility(View.VISIBLE);
        textDuration.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTvData(TvDetailModel tvDetailModel) {
        tvGenres = tvDetailModel.getGenres();
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
        cast_language = tvDetailModel.getOriginalLanguage();
        seasonsofTv = tvDetailModel.getNumberOfSeasons();
        episodes = tvDetailModel.getNumberOfEpisodes();
        if (tvDetailModel.getOverview() != null) {
            overview = tvDetailModel.getOverview();
        } else {
            overview = "Not Avalible!";
        }
        if (tvDetailModel.getHomepage().isEmpty()) {
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
        }

        // show tv data
        category.setText(sb);
        language.setText(cast_language);
        ratingBar.setRating(rates);
        ratingBar.setVisibility(View.VISIBLE);
        date.setText(convertedDate);
        description.setText(overview);
        season.setText(Integer.toString(seasonsofTv));
        episodesTv.setText(Integer.toString(episodes));
        if (Build.VERSION.SDK_INT >= 24) {
            homepage.setText(Html.fromHtml(url, Html.FROM_HTML_MODE_LEGACY));
        } else {
            homepage.setText(Html.fromHtml(url));
        }
        textSeason.setVisibility(View.VISIBLE);
        season.setVisibility(View.VISIBLE);
        episodesTv.setVisibility(View.VISIBLE);
        textofEpisode.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(tvName);
        collapsingToolbarLayout.setTitle(tvName);
    }

    @Override
    public void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressbar() {
        progressBar.setVisibility(View.GONE);
    }
}
