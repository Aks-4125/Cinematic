package com.training.cinematic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.name)
    TextView fullname;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.password)
    TextView password;
    Intent intent;
    private static final String TAG =ProfileActivity.class.getName();
    String FLAG = "flag";

    String KEY_EMAIL = "email";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        intent = getIntent();
        realm = Realm.getDefaultInstance();

       email.setText(SharedPrefsHelp.getString(ProfileActivity.this,getString(R.string.get_email_pref),""));
       /* SharedPreferences mypref = getSharedPreferences(FLAG, 0);
        email.setText(mypref.getString(KEY_EMAIL, ""));*/
        String semail = email.getText().toString();


        realm.beginTransaction();
        User user = realm.where(User.class)
                .equalTo("emailId", semail)
                .findFirst();
        if (user != null) {
            number.setText(user.getPhoneNumber());
            fullname.setText(user.getFullName());
            password.setText(user.getPassword());
        }
        realm.commitTransaction();

    }
}
