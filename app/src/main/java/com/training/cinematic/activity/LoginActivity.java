package com.training.cinematic.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.training.cinematic.Model.User;
import com.training.cinematic.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class LoginActivity extends BaseActivity  {

    @BindView(R.id.edt_email)
    EditText email;
    @BindView(R.id.edt_password)
    EditText password;
    @BindView(R.id.btn_login)
    Button login;
    @BindView(R.id.btn_signup)
    Button signup;
    private Realm realm;
    private static final String TAG = LoginActivity.class.getName();
    String FLAG = "flag";
    String KEY_EMAIL = "email";
    String KEY_PWD = "password";
    String NAME = "name";
    String email1 = "email";
    String password1 = "password";
 /*   @BindView(R.id.frame_progress)
    View progressBar;*/
    boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();



        SharedPreferences sharedPreferences = getSharedPreferences(FLAG, 0);
        if (sharedPreferences.getBoolean("logged", false)) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }



    @OnClick(R.id.btn_signup)
    public void Submit() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)

    public void OnViewClicked() {

        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
       // progressBar.setVisibility(View.VISIBLE);
        if (vaildate(email1, password1)) {
            try {
                if (isConnected()) {
                    if (checkUserNew(email1, password1)) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                      //  progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                        Toast.makeText(this, "Login Succesfully", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                      //  progressBar.setVisibility(View.GONE);
                    }


                } else {
                    Toast.makeText(this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                //  progressBar.setVisibility(View.GONE);

                }

            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                Snackbar.make(findViewById(R.id.edt_password), "Wrong Password", Snackbar.LENGTH_LONG).show();
               // progressBar.setVisibility(View.GONE);

            }
        }


    }

    public void onDestroy() {
        super.onDestroy();
        realm.close();
     //   progressBar.setVisibility(View.GONE);

    }


    private String getTrimedEmail() {
        return email.getText().toString();
    }

    private String getPassword() {
        return password.getText().toString();
    }


    private boolean vaildate(String email1, String password1) {

        if (email1.trim().equals("")) {
            Snackbar.make(findViewById(R.id.edt_email), "Email is required", Snackbar.LENGTH_LONG).show();
        //    progressBar.setVisibility(View.GONE);
            return false;
        } else if (!isEmailVaild(email1)) {
            Snackbar.make(findViewById(R.id.edt_email), "Invalid Email", Snackbar.LENGTH_LONG).show();
          //  progressBar.setVisibility(View.GONE);
            return false;
        }
        if (password1.trim().equals("")) {
            Snackbar.make(findViewById(R.id.edt_password), "Password is Required", Snackbar.LENGTH_LONG).show();
          //  progressBar.setVisibility(View.GONE);
            return false;
        } else if (!isPasswrodValid(password1)) {
            Snackbar.make(findViewById(R.id.edt_password), "Password must contain at least 8 characters", Snackbar.LENGTH_LONG).show();
          //  progressBar.setVisibility(View.GONE);
            return false;
        }

        return true;


    }


    public boolean isEmailVaild(String email1) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email1);
        return matcher.matches();

    }

    public boolean isPasswrodValid(String password1) {
        return password1.length() >= 8;
    }

    private boolean checkUser(String email, String password) {
        RealmResults<User> realmobjects = realm.where(User.class).findAll();
        for (User user : realmobjects) {
            if (email.equals(user.getEmailId()) && password.equals(user.getPassword())) {
                Log.e(TAG, user.getEmailId());


                return true;
            }
        }


        return false;
    }

    private boolean checkUserNew(String email, String password) {
        User user = realm.where(User.class)
                .equalTo("emailId", email)
                .equalTo("password", password)
                .findFirst();


        if (user != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(FLAG, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            sharedPreferences.edit().putBoolean("logged", true).apply();
            Log.e(KEY_EMAIL, user.getEmailId());
            Log.e(KEY_PWD, user.getPassword());
            editor.putString(KEY_EMAIL, email);
            editor.commit();
            finish();


            return true;
        } else {
            return false;
        }

    }

    public void onResume() {

        super.onResume();


    }


}