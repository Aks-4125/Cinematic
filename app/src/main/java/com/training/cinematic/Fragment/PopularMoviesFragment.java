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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.training.cinematic.Adapter.PopularMoviesAdapter;
import com.training.cinematic.ApiClient;
import com.training.cinematic.Model.PoplarMovieModel;
import com.training.cinematic.Model.PopularMovieResult;
import com.training.cinematic.R;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends BaseFragment {
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
    List<PopularMovieResult> movies;
    RealmList<PopularMovieResult> movieModelList;

    int movieId;

    public PopularMoviesFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        cirlcleProgressbarMovie.setVisibility(View.VISIBLE);
        realm = Realm.getDefaultInstance();

        return view;
    }

    public void startRefresh() {
        if (isConnected()) {
            retrofitFetchData();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            cirlcleProgressbarMovie.setVisibility(View.GONE);
            mRecyclerView.clearAnimation();
        }
    }

    public void onActivityCreated(@Nullable Bundle saveInstance) {
        super.onActivityCreated(saveInstance);
        cirlcleProgressbarMovie.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isConnected())
            retrofitFetchData();
        else
            getData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startRefresh();
                    }

                }, 50);


            }

        });


    }

    public void getData() {
        mRecyclerView.clearAnimation();
        swipeRefreshLayout.setRefreshing(false);
        movies = realm.where(PopularMovieResult.class)
                .findAll();
        if (movies != null) {
            Toast.makeText(getActivity(), "Fetching movies from database", Toast.LENGTH_SHORT).show();
            popularMovieAdapter = new PopularMoviesAdapter(movies, R.layout.movie_item, getActivity());
            mRecyclerView.setAdapter(popularMovieAdapter);

        }

        cirlcleProgressbarMovie.setVisibility(View.GONE);
    }

    public void retrofitFetchData() {
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isConnected()) {
         /*   realm = Realm.getDefaultInstance();
            realm.executeTransaction(realmm -> {
               RealmResults<PopularMovieResult> results=realmm.where(PopularMovieResult.class).findAll();
               results.deleteAllFromRealm();

            });*/
            ApiClient apiClient = new ApiClient(getActivity());
            apiClient.getClient()
                    .getMovielist(getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                    .enqueue(new Callback<PoplarMovieModel>() {
                        @Override
                        public void onResponse(Call<PoplarMovieModel> call, Response<PoplarMovieModel> response) {
                            movies = response.body().getResults();

                            movieModelList = response.body().getResults();

                            if (movies == null) {
                                cirlcleProgressbarMovie.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("popular movies", "popular movies size" + movies.size());
                                if (mRecyclerView != null && swipeRefreshLayout != null && cirlcleProgressbarMovie != null) {
                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(realmm -> {
                                        realmm.copyToRealmOrUpdate(movieModelList);
                                    });
                                    mRecyclerView.setAdapter(new PopularMoviesAdapter(movies, R.layout.movie_item, getActivity()));
                                    swipeRefreshLayout.setRefreshing(false);
                                    mRecyclerView.clearAnimation();
                                    cirlcleProgressbarMovie.setVisibility(View.GONE);

                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<PoplarMovieModel> call, Throwable t) {
                            Log.e(TAG, t.toString());
                            cirlcleProgressbarMovie.setVisibility(View.GONE);

                        }
                    });
            mRecyclerView.setAdapter(popularMovieAdapter);
            swipeRefreshLayout.setRefreshing(false);
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.removeAllViews();
        unbinder.unbind();
        realm.close();
    }

}
