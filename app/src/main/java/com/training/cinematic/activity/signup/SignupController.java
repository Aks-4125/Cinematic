package com.training.cinematic.activity.signup;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public interface SignupController {

    interface ISignView {
        boolean validation(String stringFullname, String stringNumber, String stringEmail, String stringPassword);
    }

    interface ISignupPresenter {
        void storeUserData(String name, String password, String email, String number);
    }

}
