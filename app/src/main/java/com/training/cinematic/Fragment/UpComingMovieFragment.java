package com.training.cinematic.Fragment;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
public class UpComingMovieFragment extends Fragment {
    private static final String TAG = UpComingMovieFragment.class.getName();
    UpComingMovieAdapter upComingMovieAdapter;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    List<MovieResult> resultSet = new ArrayList<>();
    @BindView(R.id.HeaderProgress)
    ProgressBar circleProgressbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    Unbinder unbinder;
    private MovieModel movieResponse;
    private Realm realm;
    //private View view;
    // RealmList<MovieResult> movieModel;
    private String MOVIES_POSTER_URL;

    public UpComingMovieFragment() {


    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting())
            return true;
        else
            return false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
        // realm.deleteAll();
        // new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
    /*    MovieModel movieModel = new MovieModel();
        List<MovieModel.Result> results = new ArrayList<MovieModel.Result>();
        GetJSONFromURL result = new GetJSONFromURL();*/
        return view;
    }

    //method to add content to listview while refresh
    private void startRefresh() {
        if (isConnected())
            new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
        else {
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
        new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();

        MovieModel movieModel = realm.where(MovieModel.class).findFirst();

        if (movieModel != null && !movieModel.getResults().isEmpty()) {
            Toast.makeText(getActivity(), "movies fetch from realm =" + movieModel.getResults().size(), Toast.LENGTH_SHORT).show();
            resultSet.addAll(movieModel.getResults());
            upComingMovieAdapter = new UpComingMovieAdapter(getActivity(), resultSet, R.layout.movie_item);
            mRecyclerView.setAdapter(upComingMovieAdapter);
        }
        //  getData();
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
        // mRecyclerView.setLayoutManager(layoutManager);


    }


    /*   public void onResume() {

           super.onResume();
           try (Realm r = Realm.getDefaultInstance()) {
               //   resultSet.addAll(movieResponse.getResults());

               // MovieResult movieResult = new MovieResult();
               MovieModel movieResult = realm.createObject(MovieModel.class);
               movieResult.setResults(movieResponse.getResults());
               Log.d("title", "realmtitile" + movieResult);
                          *//* movieResult.setReleaseDate(result.getTitle());
                        movieResult.setOriginalLanguage(result.getOriginalLanguage());
                        movieResult.setPosterPath(result.getPosterPath());
*//*
            r.executeTransaction(realm -> {
                realm.insert(movieResult);
                Log.d("realm result", "realm-------->" + movieResult);
            });
        }
    }*/
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        realm.close();
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

            //  circleProgressbar.setVisibility(View.VISIBLE);

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

                //  for (MovieModel movieResult:movieResponse)
                //  realm.beginTransaction();
                //  MovieModel movieResult = realm.createObject(MovieModel.class);
                //    movieResult.setResults(movieResponse.getResults());
                //  Log.d("title", "realmtitile" + movieResult);
                realm.executeTransaction(r -> {
                    r.insertOrUpdate(movieResponse);
                    //  Log.d("realm result", "realm-------->" + movieResult);
                });
/*                try (Realm r = Realm.getDefaultInstance()) {
                 //   resultSet.addAll(movieResponse.getResults());

                    // MovieResult movieResult = new MovieResult();
                        MovieModel movieResult = realm.createObject(MovieModel.class);
                        movieResult.setResults(movieResponse.getResults());
                        Log.d("title", "realmtitile" + movieResult);
                       *//* movieResult.setReleaseDate(result.getTitle());
                        movieResult.setOriginalLanguage(result.getOriginalLanguage());
                        movieResult.setPosterPath(result.getPosterPath());
*//*

                      *//*  realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<MovieResult> movieResultRealmResults = realm
                                        .where(MovieResult.class).findAll();
                                for (MovieResult result : movieResultRealmResults) {
                                    Log.d("realm result", "realm-------->" + result.getTitle());
                                }
                            }
                        });*//*

                }*/

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
            swipeRefreshLayout.setRefreshing(false);
            //   onRefreshComplete(resultSet);
            Log.d("resultset", "resultset for realm" + resultSet);
            mRecyclerView.clearAnimation();
            circleProgressbar.setVisibility(View.GONE);
            if (!movieResponse.getResults().isEmpty())
                resultSet.addAll(movieResponse.getResults());
            upComingMovieAdapter = new UpComingMovieAdapter(getActivity(), resultSet, R.layout.movie_item);
            mRecyclerView.setAdapter(upComingMovieAdapter);
            upComingMovieAdapter.notifyDataSetChanged();
            ;
           /* }
            else
            {
                Toast.makeText(getContext(), "can't fond data", Toast.LENGTH_SHORT).show();
            }*/


            // realm.insertOrUpdate(resultSet);
            //  realm.insertOrUpdate(movieModel);
            Log.d("realmresult", "Result-->" + resultSet);

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
