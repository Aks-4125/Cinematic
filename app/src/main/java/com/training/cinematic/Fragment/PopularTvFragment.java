package com.training.cinematic.Fragment;


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

import com.training.cinematic.Adapter.PopularTvAdapter;
import com.training.cinematic.ApiClient;
import com.training.cinematic.Model.PopularMovieResult;
import com.training.cinematic.Model.TvModel;
import com.training.cinematic.Model.TvResult;
import com.training.cinematic.R;

import java.util.List;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class PopularTvFragment extends BaseFragment {

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


    public PopularTvFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();

        return view;
    }

    private void startRefresh() {
        if (isConnected()) {
            retrofitData();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            mRecyclerView.clearAnimation();
            circleProgressbarTv.setVisibility(View.GONE);
        }
    }


    public void onActivityCreated(@Nullable Bundle saveInstance) {
        super.onActivityCreated(saveInstance);
        swipeRefreshLayout.setRefreshing(false);
        circleProgressbarTv.setVisibility(View.VISIBLE);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isConnected())
            retrofitData();
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
        popularTv = realm.where(TvResult.class).findAll();
        if (popularTv != null) {
            Toast.makeText(getActivity(), "Fetching movies from database", Toast.LENGTH_SHORT).show();
            tvAdapter = new PopularTvAdapter(getActivity(), R.layout.movie_item, popularTv);
            mRecyclerView.setAdapter(tvAdapter);
            mRecyclerView.clearAnimation();
            circleProgressbarTv.setVisibility(View.GONE);
        }



    }

    public void retrofitData() {
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isConnected()) {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                RealmResults<PopularMovieResult> results=realm1.where(PopularMovieResult.class).findAll();
                results.deleteAllFromRealm();
            });
            ApiClient apiClient = new ApiClient(getActivity());
            apiClient.getClient()
                    .getTvList(getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                    .enqueue(new Callback<TvModel>() {
                        @Override
                        public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                            popularTv = response.body().getResults();
                            if (popularTv == null) {
                                circleProgressbarTv.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("TAG", "Popular Tv list--->" + popularTv.size());

                                if (mRecyclerView != null && swipeRefreshLayout != null && circleProgressbarTv != null) {

                                    realm = Realm.getDefaultInstance();
                                    realm.executeTransaction(realm1 -> {
                                        realm1.copyToRealmOrUpdate(popularTv);
                                    });
                                    mRecyclerView.setAdapter(new PopularTvAdapter(getActivity(), R.layout.movie_item, popularTv));
                                    mRecyclerView.clearAnimation();
                                    swipeRefreshLayout.setRefreshing(false);
                                    circleProgressbarTv.setVisibility(View.GONE);

                                }
                            }


                        }

                        @Override
                        public void onFailure(Call<TvModel> call, Throwable t) {
                            Log.e(TAG, t.toString());
                            circleProgressbarTv.setVisibility(View.GONE);
                        }


                    });
            mRecyclerView.setAdapter(tvAdapter);
            swipeRefreshLayout.setRefreshing(false);

        }
        else {
            getData();
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
