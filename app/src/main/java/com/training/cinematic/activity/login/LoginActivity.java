package com.training.cinematic.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;
import com.training.cinematic.activity.BaseActivity;
import com.training.cinematic.activity.MainActivity;
import com.training.cinematic.activity.SignupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class LoginActivity extends BaseActivity implements LoginController.ILoginView {

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
    private LoginPresenter loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        loginPresenter = new LoginPresenter(this);
        loginPresenter.setLoginView(this);

        if (SharedPrefsHelp.getBoolean(LoginActivity.this, getString(R.string.get_loggedin_pref), false)) {
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
        String stringEmail = email.getText().toString();
        String stringPassword = password.getText().toString();
        if (vaildate(stringEmail, stringPassword)) {
            try {
                if (isConnected()) {
                    if (loginPresenter.checkUserNew(stringEmail, stringPassword)) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(this, "Login Succesfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                }
            } catch (RealmPrimaryKeyConstraintException e) {
                e.printStackTrace();
                Snackbar.make(password, "Wrong Password", Snackbar.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public boolean vaildate(String stringEmail, String stringPassword) {
        if (stringEmail.trim().equals("")) {
            Snackbar.make(email, "Email is required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!loginPresenter.isEmailVaild(stringEmail)) {
            Snackbar.make(email, "Invalid Email", Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (stringPassword.trim().equals("")) {
            Snackbar.make(password, "Password is Required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!loginPresenter.isPasswrodValid(stringPassword)) {
            Snackbar.make(password, "Password must contain at least 8 characters", Snackbar.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    //get data without using realm query
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

    public void onDestroy() {
        super.onDestroy();
        realm.close();

    }


}