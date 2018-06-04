package com.training.cinematic.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.training.cinematic.Adapter.PopularMoviesAdapter;
import com.training.cinematic.ApiKeyForMovieInterface;
import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.PopularMoviesRetorfit;
import com.training.cinematic.R;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends Fragment {
    private static final String TAG = "upcoming Movie fragment";
    Unbinder unbinder;
    PopularMoviesAdapter popularMovieAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressBar;
    @BindView(R.id.HeaderProgress)
    ProgressBar cirlcleProgressbarMovie;
    /* private MovieModel movieresponce;
     int movieimage[] = {R.drawable.cardb, R.drawable.blur, R.drawable.pin, R.drawable.newback, R.drawable.blackba};
     String moviename[];*/
    private static int API_KEY = R.string.apikeyformovie;

    public PopularMoviesFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //  moviename = getResources().getStringArray(R.array.mname);

        unbinder = ButterKnife.bind(this, view);
        cirlcleProgressbarMovie.setVisibility(View.VISIBLE);


        return view;
    }

    public void startRefresh() {
        retrofitFetchData();
    }

    public void onActivityCreated(@Nullable Bundle saveInstance) {
        super.onActivityCreated(saveInstance);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRefresh();
                        final Animation animation = new AlphaAnimation((float) 0.5, 0);
                        animation.setDuration(100);
                        animation.setInterpolator(new LinearInterpolator());
                        animation.setRepeatCount(Animation.INFINITE);
                        animation.setRepeatMode(Animation.REVERSE);
                        mRecyclerView.startAnimation(animation);


                    }

                }, 1000);


            }

        });

        retrofitFetchData();


    }

    public void retrofitFetchData() {


        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        ApiKeyForMovieInterface apiKeyForMovieInterface = PopularMoviesRetorfit.getPopularMovies().create(ApiKeyForMovieInterface.class);
        Call<MovieModel> call = apiKeyForMovieInterface.getMovielist(getString(API_KEY));
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                List<MovieModel.Result> movies = response.body().getResults();
                if (movies == null) {
                    cirlcleProgressbarMovie.setVisibility(View.VISIBLE);
                } else {
                    Log.d("popular movies", "popular movies size" + movies.size());
                    if (mRecyclerView != null && swipeRefreshLayout != null && cirlcleProgressbarMovie != null) {

                        mRecyclerView.setAdapter(new PopularMoviesAdapter(movies, R.layout.movie_item, getActivity()));
                        swipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.clearAnimation();
                        cirlcleProgressbarMovie.setVisibility(View.GONE);

                    }
                }

            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.e(TAG, t.toString());
                cirlcleProgressbarMovie.setVisibility(View.GONE);
            }
        });
        mRecyclerView.setAdapter(popularMovieAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
