package com.elblasy.wasayldriver.utiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.InputStream;

import static android.content.Context.MODE_PRIVATE;

public class SharedPref {
    // Sharedpref file name
    private static final String PREFER_NAME = "UserData";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    private Context context;


    @SuppressLint("CommitPrefEdits")
    public SharedPref(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, MODE_PRIVATE);
        editor = pref.edit();
    }

    public static String getSessionValue(String key) {
        return pref.getString(key, "ar");
    }

    //Set current tab position
    public void setPreferName(String name) {
        editor.putString("Name", name);
        editor.apply();
    }

    public void setPrefPhoneNumber(String phoneNumber) {
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }

    public void setPrefCity(String city) {
        editor.putString("City", city);
        editor.apply();
    }

    public void setPrefLang(String lan) {
        editor.putString("Language", lan);
        editor.apply();
    }

    public void setPrefToken(String token) {
        editor.putString("Token", token);
        editor.apply();
    }

    public void setProfileImage(String stream) {
        editor.putString("Image", stream);
        editor.apply();
    }

    public void checkFirstLaunch() {

        if (pref.getBoolean("firstrun", true)) {
            editor.putBoolean("firstrun", false).apply();
            setPrefLang(LocaleUtils.LAN_ARABIC);
        }

    }
}

