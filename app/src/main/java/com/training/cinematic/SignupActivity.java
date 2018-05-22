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
import io.realm.SyncCredentials;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.edt_fullname)
    EditText fullname;
    @BindView(R.id.edt_phonenumber)
    EditText phonenumber;
    @BindView(R.id.edt_password)
    EditText password;
    @BindView(R.id.edt_email)
    EditText email;
    @BindView(R.id.btn_signup)
    Button signup;
    Realm realm;
    private static final String TAG = "Sign up Activity";
    private static final String KEY_EMAIL = "email";
    private static final String NUMBER = "number";
    private static final String KEY_NAME = "name";
    private static final String KEY_PWD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();


    }

    @OnClick(R.id.btn_signup)
    public void OnViewClick() {
        String sname = fullname.getText().toString();
        String snumber = phonenumber.getText().toString();
        String semail = email.getText().toString();
        String spassword = password.getText().toString();

        if (Validation(sname, snumber, semail, spassword)) {
            try {
                realm.beginTransaction();
                User user = new User();
                user.setFullname(getFullname());
                user.setPhonenumber(getphonenumber());
                user.setEmailid(getemailid());
                user.setPassword(getpassword());
                realm.copyToRealm(user);
                realm.commitTransaction();
                SharedPreferences sharedPreferences = getSharedPreferences(TAG, 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("FULLNAME", sname);
                editor.putString("NUMBER", snumber);
                editor.putString("KEY_EMAIL", semail);
                editor.putString("KEY_PWD", spassword);
                editor.commit();
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                Toast.makeText(this, "Registration suceesfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                email.setError("Email already exist");
                realm.commitTransaction();
            }
        }

    }

    public void onDestroy() {

        super.onDestroy();
        realm.close();
    }

    public String getFullname() {
        return fullname.getText().toString();

    }

    public String getphonenumber() {
        return phonenumber.getText().toString();
    }

    public String getemailid() {
        return email.getText().toString();
    }

    public String getpassword() {
        return password.getText().toString();
    }

    public boolean Validation(String name, String number, String emaill, String passwordd) {
        if (name.trim().equals("")) {
            fullname.setError("Name is Requried");
            return false;
        } else if (number.equals("")) {
            phonenumber.setError("Number is Required");
            return false;
        } else if (!isValidphone(number)) {
            phonenumber.setError("Invalid phone number");
            return false;
        } else if (emaill.trim().equals("")) {
            email.setError("Email is Required");
            return false;
        } else if (!isEmailvalid(emaill)) {
            email.setError("Invalid Email Id");
            return false;
        } else if (passwordd.equals("")) {
            password.setError("Password is Required");
            return false;
        } else if (!isPasswordvalid(passwordd)) {
            password.setError("Password must contain at least 8 characters");
            return false;
        }
        return true;
    }

    public boolean isEmailvalid(String emaill) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(emaill);
        return matcher.matches();

    }

    public boolean isValidphone(String number) {
        Pattern pattern = Patterns.PHONE;
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public boolean isPasswordvalid(String passwordd) {
        return passwordd.length() >= 8;
    }
}
