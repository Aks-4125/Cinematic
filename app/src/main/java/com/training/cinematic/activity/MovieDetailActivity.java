package com.training.cinematic.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.training.cinematic.Adapter.Slider_adapter;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class MovieDetailActivity extends AppCompatActivity {
    @BindView(R.id.viewpager1)
     ViewPager viewPager;
    @BindView(R.id.circleindicator)
    CircleIndicator indicator;

    private static int currentpage = 0;
    private static final Integer[] abc = {R.drawable.blackba, R.drawable.pin, R.drawable.blur, R.drawable.bg_screen, R.drawable.newback};
    private ArrayList<Integer> array = new ArrayList<Integer>();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        collapsingToolbarLayout.setTitle("Movie");
        new GetJSONFromURL("https://api.themoviedb.org/3/movie/351286/images?api_key=fec13c5a0623fefac505 5a3f7b823553 ").execute();
        GetJSONFromURL data=new GetJSONFromURL();
        data.execute();
        init();
    }
    private void init() {
        for (int i = 0; i < abc.length; i++)
            array.add(abc[i]);
        MovieModel move=new MovieModel();
        viewPager.setAdapter(new Slider_adapter(MovieDetailActivity.this, array));
        indicator.setViewPager(viewPager);

        final Handler handeer = new Handler();
        final Runnable run = new Runnable() {
            @Override
            public void run() {
                if (currentpage == abc.length) {
                    currentpage = 0;
                }
                viewPager.setCurrentItem(currentpage++, true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handeer.post(run);
            }
        }, 2500, 2500);
    }




    public class GetJSONFromURL extends AsyncTask<String, String, String> {

        String apiUrl;
        Context context;
        String PERSON_KEY;
        String FILE="file";
        String DETAILS="details";

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
                Log.d("API RESPONSE", "response detail movie ------------------> " + pathToStore);

               /* MovieModel movieResponse = new Gson().fromJson(pathToStore, MovieModel.class);
                Log.d("API RESPONSE", "movieResponse size ------------------> " + movieResponse.getResults().size());
                SharedPreferences mypref=context.getSharedPreferences(FILE,0);
                SharedPreferences.Editor editor=mypref.edit();
                Gson gson=new Gson();
                String json=gson.toJson(movieResponse);
                editor.putString(DETAILS,json).apply();
                Log.e("data",DETAILS);
                Bundle bundle = new Bundle();
                bundle.putString("PERSON_KEY", movieResponse.getResults().toString());
                // bundle.putInt("YEAR_KEY", Integer.valueOf(launchYearSpinner.getSelectedItem().toString()));

*/

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage(), e);

            }

            return pathToStore;
        }
        @Override
        protected void onPostExecute(String fileDownload) {
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
