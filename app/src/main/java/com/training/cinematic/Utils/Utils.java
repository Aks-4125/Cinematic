package com.training.cinematic.Utils;

import android.content.Context;
import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dhruvisha on 7/10/2018.
 */

public class Utils {
    private Context context;

    public Utils(Context context) {
        this.context = context;
    }

    public boolean isEmailVaild(String stringEmail) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(stringEmail);
        return matcher.matches();
    }

    public boolean isPasswrodValid(String stringPassword) {
        return stringPassword.length() >= 8;
    }

    public boolean isValidphone(String stringNumber) {
        Pattern pattern = Patterns.PHONE;
        Matcher matcher = pattern.matcher(stringNumber);
        return matcher.matches();
    }

    public String convertDate(String movieDate,Context context) {
         SimpleDateFormat dateFormat;
         Date date1;
         String convertedDate = "";
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            date1 = dateFormat.parse(movieDate);
            convertedDate = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

}
