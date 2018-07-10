package com.training.cinematic.Fragment.poplarmovie;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.training.cinematic.Adapter.PopularMoviesAdapter;
import com.training.cinematic.Fragment.BaseFragment;
import com.training.cinematic.Model.PopularMovieResult;
import com.training.cinematic.R;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularMoviesFragment extends BaseFragment implements PopularMovieController.IPopularMovieView {
    private static final String TAG = "upcoming Movie fragment";
    Unbinder unbinder;
    PopularMoviesAdapter popularMovieAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.HeaderProgress)
    ProgressBar cirlcleProgressbarMovie;
    private Realm realm;
    List<PopularMovieResult> movies;
    PopularMoviePresenter popularMoviePresenter;

    public PopularMoviesFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        showProgressBar();
        realm = Realm.getDefaultInstance();
        popularMoviePresenter = new PopularMoviePresenter(getActivity());
        popularMoviePresenter.setPopularMovieView(this);
        return view;
    }

    public void onActivityCreated(@Nullable Bundle saveInstance) {
        super.onActivityCreated(saveInstance);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        showProgressBar();
        stopRefreshing();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startReshreshing();
                    }
                }, 50);
            }

        });
        if (isConnected())
            retrofitFetchData();
        else
            setOfflineData();

    }

    @Override
    public void startReshreshing() {
        if (isConnected()) {
            popularMoviePresenter.deleteData();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            stopRefreshing();
            stopProgressBar();
            mRecyclerView.clearAnimation();
        }
    }

    @Override
    public void setOfflineData() {
        movies = realm.where(PopularMovieResult.class)
                .findAll();
        stopRefreshing();
        Toast.makeText(getActivity(), "Fetching movies from database", Toast.LENGTH_SHORT).show();
        popularMovieAdapter = new PopularMoviesAdapter(movies, R.layout.movie_item, getActivity());
        mRecyclerView.setAdapter(popularMovieAdapter);
        stopProgressBar();
    }

    @Override
    public void setMovieData(List<PopularMovieResult> movies) {
        popularMovieAdapter = (new PopularMoviesAdapter(movies, R.layout.movie_item, getActivity()));
        mRecyclerView.setAdapter(popularMovieAdapter);
    }

    public void retrofitFetchData() {
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        popularMoviePresenter.deleteData();
        popularMoviePresenter.storeDataUsingRetrofit(getActivity());
    }


    @Override
    public void showProgressBar() {
        cirlcleProgressbarMovie.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        cirlcleProgressbarMovie.setVisibility(View.GONE);
    }


    @Override
    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.removeAllViews();
        unbinder.unbind();
    }
}
