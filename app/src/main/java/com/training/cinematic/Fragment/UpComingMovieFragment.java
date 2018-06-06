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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.training.cinematic.Adapter.UpComingMovieAdapter;
import com.training.cinematic.Model.MovieModel;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingMovieFragment extends Fragment {
    private static final String TAG = UpComingMovieFragment.class.getName();
    UpComingMovieAdapter upComingMovieAdapter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    List<MovieModel.Result> resultSet = new ArrayList<>();
    @BindView(R.id.HeaderProgress)
    ProgressBar circleProgressbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private MovieModel movieResponse;

    public UpComingMovieFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        // new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
    /*    MovieModel movieModel = new MovieModel();
        List<MovieModel.Result> results = new ArrayList<MovieModel.Result>();
        GetJSONFromURL result = new GetJSONFromURL();*/
        return view;
    }

    //method to add content to listview while refresh
    private void startRefresh() {
        new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
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
                        new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();

                    }

                }, 1000);


            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

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
            circleProgressbar.setVisibility(View.VISIBLE);

            if (resultSet == null) {
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
                //   Log.d(getTag(url), "apiUrl is:" + url);
                URLConnection conection = url.openConnection();
                conection.connect();
                conection.setConnectTimeout(600000);
                conection.setReadTimeout(600000);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                pathToStore = convertStreamToString(in);
                // getting file length
                int lenghtOfFile = conection.getContentLength();
                Log.d("TAG", "response of apiUrl ------------------> " + pathToStore);
                movieResponse = new Gson().fromJson(pathToStore, MovieModel.class);
                Log.d("TAG", "movieResponse size ------------------> " + movieResponse.getResults().size());
                Log.e("TAG", "Dates------------->" + movieResponse.getDates().toString());


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage(), e);

            }
            return pathToStore;

        }

        @Override
        protected void onPostExecute(String fileDownload) {
            //     Log.e("size",fileDownload);
           /* JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject) parser.parse(fileDownload);
            MovieModel movieModel = new MovieModel();
            movieModel=new Gson().fromJson(fileDownload,MovieModel.class);
            Log.e("mocvierwiifg",movieModel.toString());
            if (movieModel.getDates() != null) {
                for (int i = 0; i < 30; i++) {
                    moviename[i] = movieModel.getDates().toString();

                }
                Log.d("API RESPONSE", "data form gson is " + moviename);

            }*/

            resultSet.clear();
            //   onRefreshComplete(resultSet);
            resultSet.addAll(movieResponse.getResults());
            upComingMovieAdapter = new UpComingMovieAdapter(getActivity(), resultSet);
            mRecyclerView.setAdapter(upComingMovieAdapter);
            mRecyclerView.clearAnimation();
            swipeRefreshLayout.setRefreshing(false);
            circleProgressbar.setVisibility(View.GONE);


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
