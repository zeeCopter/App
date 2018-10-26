package com.example.hk.transport.Utilities;

import android.app.Activity;

import com.example.hk.transport.Utilities.Pojos.LoginPojo;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    public static String registerMobileNumber;
    public static String registerFirstName;
    public static String registerLastName;
    public static String registerEmail;

    public static LoginPojo loginPojo;
    public static KProgressHUD kProgressHUD;

    public static void showProgressDialog(Activity activity)
    {
        if(kProgressHUD != null && kProgressHUD.isShowing())
            kProgressHUD.dismiss();
        else
            kProgressHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public static void dismissProgressDialog()
    {
        kProgressHUD.dismiss();
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }
}
