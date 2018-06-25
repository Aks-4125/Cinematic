package com.training.cinematic.activity.signup;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public interface SignupController {
    interface ISignView {
        boolean Validation(String stringFullname, String stringNumber, String stringEmail, String stringPassword);
    }

    interface ISignupPresenter {

        boolean isEmailvalid(String stringEmail);

        boolean isValidphone(String stringNumber);

        boolean isPasswordvalid(String stringPassword);
    }

}
