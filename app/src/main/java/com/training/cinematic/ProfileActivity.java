package com.training.cinematic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.training.cinematic.Model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.name)
    EditText fullname;
    @BindView(R.id.txt_email)
    TextView temail;
    @BindView(R.id.txt_name)
    TextView namee;
    @BindView(R.id.number)
    EditText number;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    Intent intent;
    private static final String TAG = "profile activity";
    String FLAG = "flag";

    String KEY_EMAIL = "email";
    String NUMBER = "number";
    String KEY_NAME = "name";
    String KEY_PWD = "password";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        intent = getIntent();
        realm = Realm.getDefaultInstance();

        SharedPreferences mypref = getSharedPreferences(FLAG, 0);
        /*fullname.setText(mypref.getString(KEY_NAME, ""));
        number.setText(mypref.getString(NUMBER, ""));*/
        email.setText(mypref.getString(KEY_EMAIL, ""));
        String semail = email.getText().toString();
        //  password.setText(mypref.getString(KEY_PWD, ""));


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
