package com.training.cinematic.Fragment;


import android.os.AsyncTask;
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

import com.google.gson.Gson;
import com.training.cinematic.Adapter.UpComingMovieAdapter;
import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.Model.MovieResult;
import com.training.cinematic.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingMovieFragment extends BaseFragment {
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

    public UpComingMovieFragment() {

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

    //method to add content to listview while refresh
    private void startRefresh() {
        if (isConnected()) {
            new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
        } else {
            mRecyclerView.clearAnimation();
            Toast.makeText(getActivity(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
            circleProgressbar.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        circleProgressbar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        if (isConnected())
            new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
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


                }, 2000);


            }

        });


    }

    public void getData() {
        MovieModel movieModel = realm.where(MovieModel.class).findFirst();
        if (movieModel != null && !movieModel.getResults().isEmpty()) {
            Toast.makeText(getActivity(), "Fetching movies from database", Toast.LENGTH_SHORT).show();
            movieResultArrayList.addAll(movieModel.getResults());
            upComingMovieAdapter = new UpComingMovieAdapter(getActivity(), movieResultArrayList, R.layout.movie_item);
            mRecyclerView.setAdapter(upComingMovieAdapter);
            circleProgressbar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.removeAllViews();
        unbinder.unbind();
    }


    public class GetJSONFromURL extends AsyncTask<String, String, String> {

        String apiUrl;

        public GetJSONFromURL(String fileDownload) {
            this.apiUrl = fileDownload;
        }

        public GetJSONFromURL() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            if (movieResultArrayList == null) {
                swipeRefreshLayout.setRefreshing(true);
            } else {
                swipeRefreshLayout.setRefreshing(false);
            }


            // do is success false

        }

        @Override
        protected String doInBackground(String... fileDownloads) {
            String pathToStore = "";
            try {

                URL url = new URL(apiUrl);
                URLConnection conection = url.openConnection();
                conection.connect();
                conection.setConnectTimeout(600000);
                conection.setReadTimeout(600000);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                pathToStore = convertStreamToString(in);
                Log.d("TAG", "response of apiUrl ------------------> " + pathToStore);
                movieResponse = new Gson().fromJson(pathToStore, MovieModel.class);
                Log.d("TAG", "movieResponse size ------------------> " + movieResponse.getResults().size());
                Log.e("TAG", "Dates------------->" + movieResponse.getDates().toString());


                realm = Realm.getDefaultInstance();
                realm.executeTransaction(realmu -> {
                    realmu.insertOrUpdate(movieResponse);
                });
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage(), e);

            }
            return pathToStore;

        }

        @Override
        protected void onPostExecute(String fileDownload) {

            movieResultArrayList.clear();
            swipeRefreshLayout.setRefreshing(false);
            Log.d("resultset", "resultset for realm" + movieResultArrayList);
            mRecyclerView.clearAnimation();
            circleProgressbar.setVisibility(View.GONE);
            if (!movieResponse.getResults().isEmpty())
                movieResultArrayList.addAll(movieResponse.getResults());
            upComingMovieAdapter = new UpComingMovieAdapter(getActivity(), movieResultArrayList, R.layout.movie_item);
            mRecyclerView.setAdapter(upComingMovieAdapter);
            upComingMovieAdapter.notifyDataSetChanged();

            Log.d("realmresult", "Result-->" + movieResultArrayList);

        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }
}
