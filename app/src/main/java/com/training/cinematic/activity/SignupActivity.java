package com.training.cinematic.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;
import com.training.cinematic.activity.login.LoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class SignupActivity extends BaseActivity {

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
    private static final String TAG = SignupActivity.class.getName();
    String FLAG = "flag";
    String KEY_EMAIL = "email";
    String NUMBER = "number";
    String KEY_NAME = "name";
    String KEY_PWD = "password";
    SharedPreferences.Editor editor;

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
                try (Realm r = Realm.getDefaultInstance()) {
                    if (isConnected()) {
                        final User user = new User();
                        user.setFullName(sname);
                        user.setPhoneNumber(snumber);
                        user.setEmailId(semail);
                        user.setPassword(spassword);
                        SharedPrefsHelp.setObject(SignupActivity.this,getString(R.string.get_user_pref),user);
                      /*  SharedPreferences sharedPreferences = getSharedPreferences(FLAG, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Log.e(KEY_NAME, user.getFullName());
                        Log.e(NUMBER, user.getPhoneNumber());
                        Log.e(KEY_EMAIL, user.getEmailId());
                        Log.e(KEY_PWD, user.getPassword());
                        editor.commit();
*/
                        r.executeTransaction(realm -> {
                            realm.insert(user);
                        });
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        Toast.makeText(this, "Registration suceesfully", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                }


            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                Snackbar.make(findViewById(R.id.edt_email), "Email already exist", Snackbar.LENGTH_LONG).show();
            }
        }

    }

    public void onDestroy() {

        super.onDestroy();
        realm.close();
    }


    public boolean Validation(String name, String number, String emaill, String passwordd) {
        if (name.trim().equals("")) {
            Snackbar.make(findViewById(R.id.edt_fullname), "Name is Requried", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (number.equals("")) {
            Snackbar.make(findViewById(R.id.edt_phonenumber), "Number is Required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!isValidphone(number)) {
            Snackbar.make(findViewById(R.id.edt_phonenumber), "Invalid phone number", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (emaill.trim().equals("")) {
            Snackbar.make(findViewById(R.id.edt_email), "Email is Required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!isEmailvalid(emaill)) {
            Snackbar.make(findViewById(R.id.edt_email), "Invalid Email Id", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (passwordd.equals("")) {
            Snackbar.make(findViewById(R.id.edt_password), "Password is Required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!isPasswordvalid(passwordd)) {
            Snackbar.make(findViewById(R.id.edt_password), "Password must contain at least 8 characters", Snackbar.LENGTH_LONG).show();
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
