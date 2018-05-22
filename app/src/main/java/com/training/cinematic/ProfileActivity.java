package com.training.cinematic;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.training.cinematic.Model.User;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.name)
    EditText fullname;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    private static final String TAG = "profile activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        String sname = fullname.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(TAG, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        sharedPreferences.getString("FULLNAME", sname);
        editor.commit();
    }
}
