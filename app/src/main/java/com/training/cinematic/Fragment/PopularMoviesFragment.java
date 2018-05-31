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

import com.training.cinematic.Adapter.PopularMoviesAdapter;
import com.training.cinematic.ApiKeyInterface;
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
    private static final String TAG = "upcoming movie fragment";
    Unbinder unbinder;
    PopularMoviesAdapter popularMovieAdapter;
    @BindView(R.id.recyclerview1)
    RecyclerView mRecyclerView;
   /* private MovieModel movieresponce;
    int movieimage[] = {R.drawable.cardb, R.drawable.blur, R.drawable.pin, R.drawable.newback, R.drawable.blackba};
    String moviename[];*/
    private static int API_KEY=R.string.apikey;

    public PopularMoviesFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_popularmovies, container, false);
      //  moviename = getResources().getStringArray(R.array.mname);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    public void onActivityCreated(@Nullable Bundle saveInstance) {
        super.onActivityCreated(saveInstance);


        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        ApiKeyInterface apiKeyInterface = PopularMoviesRetorfit.getdata().create(ApiKeyInterface.class);
        Call<MovieModel> call=apiKeyInterface.getMovielist(getString(API_KEY));
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
                List<MovieModel.Result> movies=response.body().getResults();
                Log.d("popular movies","popular movies size"+movies.size());
                mRecyclerView.setAdapter(new PopularMoviesAdapter(movies,R.layout.movie_item,getActivity()));
            }

            @Override
            public void onFailure(Call<MovieModel> call, Throwable t) {
                Log.e(TAG, t.toString());
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
