package com.training.cinematic.Fragment.upcomingmovie;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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

import com.training.cinematic.Adapter.UpComingMovieAdapter;
import com.training.cinematic.Fragment.BaseFragment;
import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.Model.MovieResult;
import com.training.cinematic.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingMovieFragment extends BaseFragment implements UpComingMovieController.IUpComingMovieView {
    private static final String TAG = UpComingMovieFragment.class.getName();
    UpComingMovieAdapter upComingMovieAdapter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    List<MovieResult> movieResultArrayList = new ArrayList<>();
    @BindView(R.id.HeaderProgress)
    ProgressBar circleProgressbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private MovieModel movieResponse;
    private Realm realm;
    UpComingMoviePresenter upComingMoviePresenter;

    public UpComingMovieFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        upComingMoviePresenter = new UpComingMoviePresenter(getActivity());
        upComingMoviePresenter.setUpComingMovieView(this);
        realm = Realm.getDefaultInstance();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showProgressBar();
        stopRefreshing();
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isConnected())
            upComingMoviePresenter.callJsonClass("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553");
        else
            setOfflineData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startReshreshing();
                    }

                }, 2000);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.removeAllViews();
        unbinder.unbind();
    }

    @Override
    public void showProgressBar() {
        circleProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        circleProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void startReshreshing() {
        if (isConnected()) {
            upComingMoviePresenter.deleteData();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            stopProgressBar();
            stopRefreshing();
        }
    }

    @Override
    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setOfflineData() {
        stopRefreshing();
        stopProgressBar();
        MovieModel movieModel = realm.where(MovieModel.class).findFirst();
        if (movieModel != null && !movieModel.getResults().isEmpty()) {
            Toast.makeText(getActivity(), "Fetching movies from database", Toast.LENGTH_SHORT).show();
            movieResultArrayList.addAll(movieModel.getResults());
            upComingMovieAdapter = new UpComingMovieAdapter(getActivity(), movieResultArrayList, R.layout.movie_item);
            mRecyclerView.setAdapter(upComingMovieAdapter);
        }
    }

    @Override
    public void setMovieData(List<MovieResult> movieResultArrayList) {
        upComingMovieAdapter = new UpComingMovieAdapter(getActivity(), movieResultArrayList, R.layout.movie_item);
        mRecyclerView.setAdapter(upComingMovieAdapter);
        upComingMovieAdapter.notifyDataSetChanged();
        Log.d("realmresult", "Result-->" + movieResultArrayList);
    }

}
