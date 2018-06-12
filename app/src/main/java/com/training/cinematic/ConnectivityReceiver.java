package com.training.cinematic;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by dhruvisha on 6/8/2018.
 */

public class ConnectivityReceiver extends AppCompatActivity {


    public ConnectivityReceiver() {
        super();
    }
    public void checkconnection() {
        if (isConnected()){
            Toast.makeText(this, "internet is avalible", Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(this, "internet is not avalible", Toast.LENGTH_SHORT).show();

    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        } else {
            return false;
        }



    }
}