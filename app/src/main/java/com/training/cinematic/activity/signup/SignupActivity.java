package com.training.cinematic.activity.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;
import com.training.cinematic.activity.BaseActivity;
import com.training.cinematic.activity.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class SignupActivity extends BaseActivity implements SignupController.ISignView{

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
    SignupPresenter signupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        Realm.init(this);
        signupPresenter=new SignupPresenter(this);
        signupPresenter.setSignView(this);
        realm = Realm.getDefaultInstance();

    }



    @OnClick(R.id.btn_signup)
    public void OnViewClick() {
        String stringFullname = fullname.getText().toString();
        String stringNumber = phonenumber.getText().toString();
        String stringEmail = email.getText().toString();
        String stringPassword = password.getText().toString();

        if (Validation(stringFullname, stringNumber, stringEmail, stringPassword)) {
            try {
                try (Realm r = Realm.getDefaultInstance()) {
                    if (isConnected()) {
                        final User user = new User();
                        user.setFullName(stringFullname);
                        user.setPhoneNumber(stringNumber);
                        user.setEmailId(stringEmail);
                        user.setPassword(stringPassword);
                        SharedPrefsHelp.setObject(SignupActivity.this,getString(R.string.get_user_pref),user);

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
                Snackbar.make(email, "Email already exist", Snackbar.LENGTH_LONG).show();
            }
        }

    }



    public boolean Validation(String stringFullname, String stringNumber, String stringEmail, String stringPassword) {
        if (stringFullname.trim().equals("")) {
            Snackbar.make(fullname, "Name is Requried", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (stringNumber.equals("")) {
            Snackbar.make(phonenumber, "Number is Required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!signupPresenter.isValidphone(stringNumber)) {
            Snackbar.make(phonenumber, "Invalid phone number", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (stringEmail.trim().equals("")) {
            Snackbar.make(email, "Email is Required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!signupPresenter.isEmailvalid(stringEmail)) {
            Snackbar.make(email, "Invalid Email Id", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (stringPassword.equals("")) {
            Snackbar.make(password, "Password is Required", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!signupPresenter.isPasswordvalid(stringPassword)) {
            Snackbar.make(password, "Password must contain at least 8 characters", Snackbar.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
