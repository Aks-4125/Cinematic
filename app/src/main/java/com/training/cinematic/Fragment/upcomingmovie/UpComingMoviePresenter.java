package com.training.cinematic.Fragment.upcomingmovie;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.training.cinematic.Model.MovieModel;
import com.training.cinematic.Model.MovieResult;

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

import io.realm.Realm;

/**
 * Created by dhruvisha on 6/29/2018.
 */

public class UpComingMoviePresenter implements UpComingMovieController.IUpComingMoviePresenter {
    private static final String TAG = UpComingMoviePresenter.class.getName();
    Context context;
    List<MovieResult> movieResultArrayList = new ArrayList<>();
    private Realm realm;
    private MovieModel movieResponse;
    UpComingMovieController.IUpComingMovieView upComingMovieView;

    public void setUpComingMovieView(UpComingMovieController.IUpComingMovieView upComingMovieView) {
        this.upComingMovieView = upComingMovieView;
    }

    public UpComingMoviePresenter(Context context) {
        this.context = context;
    }


    public class GetJSONFromURL extends AsyncTask<String, String, String>

    {
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
                upComingMovieView.startReshreshing();
            } else {
                upComingMovieView.stopRefreshing();
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
            upComingMovieView.stopRefreshing();
            Log.d("resultset", "resultset for realm" + movieResultArrayList);
            upComingMovieView.stopProgressBar();

            if (!movieResponse.getResults().isEmpty())
                movieResultArrayList.addAll(movieResponse.getResults());
            upComingMovieView.setMovieData(movieResultArrayList);

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


    @Override
    public void callJsonClass(String uri) {
        new GetJSONFromURL(uri).execute();
    }

    @Override
    public void deleteData() {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realmu -> {
            realmu.delete(MovieModel.class);
            realmu.delete(MovieResult.class);
        });
        new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();
    }
}


