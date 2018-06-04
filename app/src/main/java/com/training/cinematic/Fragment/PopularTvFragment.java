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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.training.cinematic.Adapter.PopularTvAdapter;
import com.training.cinematic.ApiKeyForTvInterface;
import com.training.cinematic.Model.TvModel;
import com.training.cinematic.PopularTvRetrofit;
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
public class PopularTvFragment extends Fragment {

    private static final String TAG = "upcoming Movie fragment";
    PopularTvAdapter tvAdapter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.HeaderProgress)
    ProgressBar circleProgressbarTv;
    /*  int movieimage[] = {R.drawable.cardb, R.drawable.blur, R.drawable.pin, R.drawable.newback, R.drawable.blackba};
      String moviename[];*/
    Unbinder unbinder;
    private static int API_KEY = R.string.apikeyfortv;

    public PopularTvFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //  moviename = getResources().getStringArray(R.array.mname);
        unbinder = ButterKnife.bind(this, view);
        circleProgressbarTv.setVisibility(View.VISIBLE);

        return view;
    }

    private void startRefresh() {

        retrofitData();

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

        retrofitData();


    }

    public void retrofitData() {


        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        ApiKeyForTvInterface apiKeyForTvInterface = PopularTvRetrofit.getPopularTv().create(ApiKeyForTvInterface.class);
        Call<TvModel> call = apiKeyForTvInterface.getTvList(getString(API_KEY));

        call.enqueue(new Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                List<TvModel.Result> popularTv = response.body().getResults();
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

        //   tvAdapter = new PopularTvAdapter(getActivity(), movieimage, moviename);

        mRecyclerView.setAdapter(tvAdapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
