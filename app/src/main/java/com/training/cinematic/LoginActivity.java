package com.training.cinematic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.training.cinematic.Model.User;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(TAG, 0);
        if (sharedPreferences.getString("logged", "").toString().equals("logged")) ;
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
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
        if (vaildate(email1, password1)) {
            try {
                if (checkUser(email1, password1)) {
                    SharedPreferences sharedPreferences = getSharedPreferences(TAG, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("logged", "logged");
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Login Succesfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                password.setError("Wrong password");

            }
        }


    }

    public void onDestroy() {

        super.onDestroy();
        realm.close();
    }


    private String getTrimedEmail() {
        return email.getText().toString();
    }

    private String getPassword() {
        return password.getText().toString();
    }


    private boolean vaildate(String email1, String password1) {

        if (email1.trim().equals("")) {
            email.setError("Email is Required");
            return false;
        } else if (!isEmailVaild(email1)) {
            email.setError("Invalid Email");
            return false;
        }
        if (password1.trim().equals("")) {
            password.setError("Password is Required");
            return false;
        } else if (!isPasswrodValid(password1)) {
            password.setError("Password must contain at least 8 characters");
            return false;
        }

        return true;


    }


    public static boolean isEmailVaild(String email1) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email1);
        return matcher.matches();

    }

    public static boolean isPasswrodValid(String password1) {

        return password1.length() >= 8;
    }

    private boolean checkUser(String email, String password) {
        RealmResults<User> realmobjects = realm.where(User.class).findAll();
        for (User user : realmobjects) {
            if (email.equals(user.getEmailid()) && password.equals(user.getPassword())) {
                Log.e(TAG, user.getEmailid());

                return true;
            }
        }


        return false;
    }

}