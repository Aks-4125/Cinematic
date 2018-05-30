package com.training.cinematic.Fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingMovieFragment extends Fragment {
    private static final String TAG = "upcoming movie fragment";
    UpComingMovieAdapter movieadapter;
    String PERSON_KEY;
    int img[] = {R.drawable.cardb, R.drawable.blur, R.drawable.pin, R.drawable.newback, R.drawable.blackba};
    String data1[];
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
        View view = inflater.inflate(R.layout.fragment_upcomingmovie, container, false);
        data1 = getResources().getStringArray(R.array.mname);
        unbinder = ButterKnife.bind(this, view);
    /*    MovieModel movieModel = new MovieModel();
        List<MovieModel.Result> results = new ArrayList<MovieModel.Result>();
        GetJSONFromURL result = new GetJSONFromURL();*/

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        new GetJSONFromURL("https://api.themoviedb.org/3/movie/upcoming?api_key=fec13c5a0623fefac5055a3f7b823553").execute();

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
            // do is success false

        }

        @Override
        protected String doInBackground(String... fileDownloads) {
            int count;
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
                Log.d("API RESPONSE", "response of apiUrl ------------------> " + pathToStore);

                movieResponse = new Gson().fromJson(pathToStore, MovieModel.class);
                Log.d("API RESPONSE", "movieResponse size ------------------> " + movieResponse.getResults().size());
                Log.e("DATES", "Dates------------->" + movieResponse.getDates().toString());


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
            movieadapter = new UpComingMovieAdapter(getActivity(), movieResponse.getResults());
            mRecyclerView.setAdapter(movieadapter);

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
