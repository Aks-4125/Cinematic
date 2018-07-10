package com.training.cinematic.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;

import org.json.JSONObject;

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
    public boolean checkUserNew(String stringEmail, String stringPassword) {
        Realm realm = Realm.getDefaultInstance();
        User user = realm.where(User.class)
                .equalTo("emailId", stringEmail)
                .equalTo("password", stringPassword)
                .findFirst();

        if (user != null) {
            SharedPrefsHelp.setBoolean(context, "logIn", true);
            SharedPrefsHelp.setString(context, context.getString(R.string.get_email_pref), user.getEmailId());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        SharedPrefsHelp.setString(context, "userProfile", json_object.toString());
                        loginView.loginCompleteWithFb(context);
                    }
                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    @Override
    public void loginWithFb(CallbackManager callbackManager, Context context) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                Log.d("error", "cancle->");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("error", "erorrr->" + error);
            }
        });
    }
}
