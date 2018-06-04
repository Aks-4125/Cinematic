package com.training.cinematic.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edt_email)
    EditText email;
    @BindView(R.id.edt_password)
    EditText password;
    @BindView(R.id.btn_login)
    Button login;
    @BindView(R.id.btn_signup)
    Button signup;
    private Realm realm;
    private static final String TAG = "Login activity";
    String FLAG = "flag";
    String KEY_EMAIL = "email";
    String KEY_PWD = "password";
    String NAME = "name";
    String email1 = "email";
    String password1 = "password";
     ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        progressBar=new ProgressDialog(this);
        progressBar.setMessage("Please wait...");
        progressBar.setCancelable(false);
        progressBar.setIndeterminate(true);
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
        progressBar.show();
        String email1 = email.getText().toString();
        String password1 = password.getText().toString();
        if (vaildate(email1, password1)) {
            try {
                if (checkUserNew(email1, password1)) {
                    progressBar.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Login Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    progressBar.dismiss();
                }
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                Snackbar.make(findViewById(R.id.edt_password), "Wrong Password", Snackbar.LENGTH_LONG).show();
                progressBar.dismiss();

            }
        }


    }

    public void onDestroy() {
        super.onDestroy();
        realm.close();
        progressBar.dismiss();
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
            progressBar.dismiss();
            return false;
        } else if (!isEmailVaild(email1)) {
            Snackbar.make(findViewById(R.id.edt_email), "Invalid Email", Snackbar.LENGTH_LONG).show();
            progressBar.dismiss();
            return false;
        }
        if (password1.trim().equals("")) {
            Snackbar.make(findViewById(R.id.edt_password), "Password is Required", Snackbar.LENGTH_LONG).show();
            progressBar.dismiss();
            return false;
        } else if (!isPasswrodValid(password1)) {
            Snackbar.make(findViewById(R.id.edt_password), "Password must contain at least 8 characters", Snackbar.LENGTH_LONG).show();
            progressBar.dismiss();
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
        } else return false;
        //  return user != null;

    }

}