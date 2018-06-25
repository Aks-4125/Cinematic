package com.training.cinematic.activity.login;

import android.content.Context;
import android.util.Patterns;

import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.Realm;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public class LoginPresenter implements LoginController.ILoginPresenter {


    private Context context;

    private LoginController.ILoginView loginView;

    public void setLoginView(LoginController.ILoginView loginView) {
        this.loginView = loginView;
    }

    public LoginPresenter(Context context) {
        this.context = context;
    }

    @Override
    public boolean isEmailVaild(String stringEmail) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(stringEmail);
        return matcher.matches();
    }

    @Override
    public boolean isPasswrodValid(String stringPassword) {
        return stringPassword.length() >= 8;
    }

    @Override
    public boolean checkUserNew(String stringEmail, String stringPassword) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class)
                .equalTo("emailId", stringEmail)
                .equalTo("password", stringPassword)
                .findFirst();

        if (user != null) {
            SharedPrefsHelp.setBoolean(context, context.getString(R.string.get_loggedin_pref), true);
            SharedPrefsHelp.setString(context, context.getString(R.string.get_email_pref), user.getEmailId());
            return true;
        } else {
            return false;
        }
    }
}
