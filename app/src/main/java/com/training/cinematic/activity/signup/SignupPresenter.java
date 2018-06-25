package com.training.cinematic.activity.signup;

import android.content.Context;
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public class SignupPresenter implements SignupController.ISignupPresenter{
    private Context context;

    public SignupPresenter(Context context) {
        this.context = context;
    }

    private SignupController.ISignView signView;

    public void setSignView(SignupController.ISignView signView) {
        this.signView = signView;
    }

    @Override
    public boolean isEmailvalid(String stringEmail) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(stringEmail);
        return matcher.matches();

    }

    @Override
    public boolean isValidphone(String stringNumber) {
        Pattern pattern = Patterns.PHONE;
        Matcher matcher = pattern.matcher(stringNumber);
        return matcher.matches();
    }
    @Override
    public boolean isPasswordvalid(String stringPassword) {
        return stringPassword.length() >= 8;
    }
}
