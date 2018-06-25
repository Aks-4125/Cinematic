package com.training.cinematic.activity.login;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public interface LoginController {

    interface ILoginView {
        boolean vaildate(String stringEmail, String stringPassword);
    }

    interface ILoginPresenter {
        boolean isEmailVaild(String stringEmail);

        boolean isPasswrodValid(String stringPassword);

        boolean checkUserNew(String stringEmail, String stringPassword);

    }

}
