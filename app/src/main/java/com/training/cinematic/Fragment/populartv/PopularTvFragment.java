package com.training.cinematic.Fragment.populartv;


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

import com.training.cinematic.Adapter.PopularTvAdapter;
import com.training.cinematic.Fragment.BaseFragment;
import com.training.cinematic.Model.TvResult;
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
public class PopularTvFragment extends BaseFragment implements PopularTvController.IPopularTvView {

    private static final String TAG = "upcoming Movie fragment";
    PopularTvAdapter tvAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.HeaderProgress)
    ProgressBar circleProgressbarTv;
    Unbinder unbinder;
    private Realm realm;
    List<TvResult> popularTv;
    PopularTvPresenter popularTvPresenter;

    public PopularTvFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        popularTvPresenter = new PopularTvPresenter(getActivity());
        popularTvPresenter.setPopularTvView(this);
        realm = Realm.getDefaultInstance();
        return view;
    }

    public void onActivityCreated(@Nullable Bundle saveInstance) {
        super.onActivityCreated(saveInstance);
        stopRefreshing();
        showProgressBar();
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isConnected())
            retrofitData();
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


                }, 50);


            }

        });
    }

    public void retrofitData() {
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        popularTvPresenter.deleteData();
        popularTvPresenter.storeDataUsingRetrofit(getActivity());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.removeAllViews();
        unbinder.unbind();
    }

    @Override
    public void showProgressBar() {
        circleProgressbarTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopProgressBar() {
        circleProgressbarTv.setVisibility(View.GONE);
    }

    @Override
    public void startReshreshing() {
        if (isConnected()) {
            popularTvPresenter.deleteData();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            stopRefreshing();
            stopProgressBar();
            mRecyclerView.clearAnimation();
        }
    }

    @Override
    public void stopRefreshing() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setOfflineData() {
        stopRefreshing();
        popularTv = realm.where(TvResult.class).findAll();
        if (popularTv != null) {
            Toast.makeText(getActivity(), "Fetching movies from database", Toast.LENGTH_SHORT).show();
            tvAdapter = new PopularTvAdapter(getActivity(), R.layout.movie_item, popularTv);
            mRecyclerView.setAdapter(tvAdapter);
            stopProgressBar();
        }
    }

    @Override
    public void setTvData(List<TvResult> tvResults) {
        tvAdapter = new PopularTvAdapter(getActivity(), R.layout.movie_item, tvResults);
        mRecyclerView.setAdapter(tvAdapter);
    }

}
