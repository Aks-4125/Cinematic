package com.training.cinematic.activity.signup;

import android.content.Context;

import com.training.cinematic.Model.User;
import com.training.cinematic.R;
import com.training.cinematic.Utils.SharedPrefsHelp;

import io.realm.Realm;

/**
 * Created by dhruvisha on 6/25/2018.
 */

public class SignupPresenter implements SignupController.ISignupPresenter {
    private Context context;

    public SignupPresenter(Context context) {
        this.context = context;
    }

    private SignupController.ISignView signView;

    public void setSignView(SignupController.ISignView signView) {
        this.signView = signView;
    }


    @Override
    public void storeUserData(String stringFullname, String stringPassword, String stringEmail, String stringNumber) {
        try (Realm r = Realm.getDefaultInstance()) {

            final User user = new User();
            user.setFullName(stringFullname);
            user.setPhoneNumber(stringNumber);
            user.setEmailId(stringEmail);
            user.setPassword(stringPassword);
            r.executeTransaction(realm -> {
                realm.insert(user);
            });
            SharedPrefsHelp.setObject(context, context.getString(R.string.get_user_pref), user);

        }


    }
}
