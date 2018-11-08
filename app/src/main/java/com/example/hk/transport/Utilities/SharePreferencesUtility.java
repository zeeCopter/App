package com.example.hk.transport.Utilities;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.hk.transport.Utilities.Pojos.LoginPojo;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class SharePreferencesUtility {

    SharedPreferences  mPrefs;
    String PREFS_NAME = "CustomerApp";

    public SharePreferencesUtility (Activity activity)
    {
        mPrefs = activity.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public void saveLoginModel(LoginPojo loginPojo)
    {
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        prefsEditor.putString("UserId", loginPojo.getUserId());
        prefsEditor.putString("FirstName", loginPojo.getFirstName());
        prefsEditor.putString("LastName", loginPojo.getLastName());
        prefsEditor.putString("ImageUrl", loginPojo.getImageUrl());
        prefsEditor.putString("EmailAddress", loginPojo.getEmailAddress());
        prefsEditor.commit();
    }

    public LoginPojo getLoginModel()
    {
        LoginPojo loginPojo = new LoginPojo();
        loginPojo.setUserId(mPrefs.getString("UserId", ""));
        loginPojo.setFirstName(mPrefs.getString("FirstName", ""));
        loginPojo.setLastName(mPrefs.getString("LastName", ""));
        loginPojo.setImageUrl(mPrefs.getString("ImageUrl", ""));
        loginPojo.setEmailAddress(mPrefs.getString("EmailAddress", ""));
        return loginPojo;
    }
}
