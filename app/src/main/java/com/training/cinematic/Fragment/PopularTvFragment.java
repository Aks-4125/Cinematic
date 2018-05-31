package com.training.cinematic.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    @BindView(R.id.recyclerview2)
    RecyclerView mRecyclerView;
  /*  int movieimage[] = {R.drawable.cardb, R.drawable.blur, R.drawable.pin, R.drawable.newback, R.drawable.blackba};
    String moviename[];*/
    Unbinder unbinder;
    private static int API_KEY=R.string.apikeyfortv;

    public PopularTvFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_populartv, container, false);
      //  moviename = getResources().getStringArray(R.array.mname);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

    public void onActivityCreated(@Nullable Bundle saveInstance) {

        super.onActivityCreated(saveInstance);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        ApiKeyForTvInterface apiKeyForTvInterface= PopularTvRetrofit.getPopularTv().create(ApiKeyForTvInterface.class);
        Call<TvModel>call=apiKeyForTvInterface.getTvList(getString(API_KEY));
        call.enqueue(new Callback<TvModel>() {
            @Override
            public void onResponse(Call<TvModel> call, Response<TvModel> response) {
                List<TvModel.Result>popularTv=response.body().getResults();
                Log.d("TAG","Popular Tv list--->"+popularTv.size());
                mRecyclerView.setAdapter(new PopularTvAdapter(getActivity(),R.layout.movie_item,popularTv));
            }

            @Override
            public void onFailure(Call<TvModel> call, Throwable t) {
                Log.e(TAG, t.toString());
            }


        });

     //   tvAdapter = new PopularTvAdapter(getActivity(), movieimage, moviename);

        mRecyclerView.setAdapter(tvAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
