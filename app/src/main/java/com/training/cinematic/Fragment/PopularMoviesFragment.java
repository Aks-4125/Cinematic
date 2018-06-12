package com.training.cinematic.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.Toast;

import com.training.cinematic.Adapter.PopularMoviesAdapter;
import com.training.cinematic.ApiClient;
import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.Model.MovieResult;
import com.training.cinematic.R;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
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
    private Realm realm;


    public PopularMoviesFragment() {


    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
          //  cirlcleProgressbarMovie.setVisibility(View.VISIBLE);
            return true;
        }
        else
        {

            cirlcleProgressbarMovie.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            return false;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        cirlcleProgressbarMovie.setVisibility(View.VISIBLE);
        realm=Realm.getDefaultInstance();


        return view;
    }

    public void startRefresh() {
        if (isConnected()){
            retrofitFetchData();
        }
        else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            cirlcleProgressbarMovie.setVisibility(View.GONE);

        }
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
        if (isConnected()) {
            retrofitFetchData();

        }
        else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            cirlcleProgressbarMovie.setVisibility(View.GONE);
        }

    }

    public void retrofitFetchData() {


        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        ApiClient apiClient = new ApiClient(getActivity());

        apiClient.getClient()
                .getMovielist(getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                .enqueue(new Callback<MovieModel>() {
                    @Override
                    public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                        List<MovieResult> movies = response.body().getResults();
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
        realm.close();
    }

}
