package com.training.cinematic.Fragment;


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

import com.training.cinematic.Adapter.PopularTvAdapter;
import com.training.cinematic.ApiClient;
import com.training.cinematic.Model.TvModel;
import com.training.cinematic.Model.TvResult;
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
public class PopularTvFragment extends Fragment {

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


    public PopularTvFragment() {

    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            //    circleProgressbarTv.setVisibility(View.VISIBLE);
            return true;

        } else {

            circleProgressbarTv.setVisibility(View.GONE);
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
        circleProgressbarTv.setVisibility(View.VISIBLE);
        realm = Realm.getDefaultInstance();
        Realm.init(getContext());

        return view;
    }

    private void startRefresh() {
        if (isConnected()) {
            retrofitData();
        } else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            circleProgressbarTv.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
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
            retrofitData();

        } else {
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            circleProgressbarTv.setVisibility(View.GONE);


        }


    }

    public void retrofitData() {


        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        ApiClient apiClient = new ApiClient(getActivity());
        apiClient.getClient()
                .getTvList(getString(Integer.parseInt(String.valueOf(R.string.apikey))))
                .enqueue(new Callback<TvModel>() {
                    @Override
                    public void onResponse(Call<TvModel> call, Response<TvModel> response) {

                        List<TvResult> popularTv = response.body().getResults();

                        if (popularTv == null) {
                            circleProgressbarTv.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("TAG", "Popular Tv list--->" + popularTv.size());

                            if (mRecyclerView != null && swipeRefreshLayout != null && circleProgressbarTv != null) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        realm.close();
    }
}
