package com.training.cinematic;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.training.cinematic.Model.MovieModel;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by dhruvisha on 5/29/2018.
 */

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
            Log.d("API RESPONSE", "response of apiUrl ------------------> " + pathToStore);

            MovieModel movieResponse = new Gson().fromJson(pathToStore, MovieModel.class);
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