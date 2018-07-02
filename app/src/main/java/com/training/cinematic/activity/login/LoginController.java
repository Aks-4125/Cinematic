package com.training.cinematic.activity.login;

import android.content.Context;

import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public interface LoginController {

    interface ILoginView {
        boolean vaildate(String stringEmail, String stringPassword);

        void loginCompleteWithFb(Context context);

    }

    interface ILoginPresenter {
        boolean isEmailVaild(String stringEmail);

        boolean isPasswrodValid(String stringPassword);

        boolean checkUserNew(String stringEmail, String stringPassword);

        void getUserDetails(LoginResult loginResult);

        void loginWithFb(CallbackManager callbackManager, final Context context);

    }

}
