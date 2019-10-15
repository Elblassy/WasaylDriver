package com.elblasy.wasayldriver.utiles;

import android.app.Application;
import android.content.res.Configuration;

import java.util.Locale;

public class Wasayl extends Application {


    @Override
    public void onCreate() {

        SharedPref sharedPref = new SharedPref(this);
        sharedPref.checkFirstLaunch();
        super.onCreate();

        if (SharedPref.getSessionValue("Language") != null)
            LocaleUtils.setLocale(new Locale(SharedPref.getSessionValue("Language")));
        else
            LocaleUtils.setLocale(new Locale(LocaleUtils.LAN_ARABIC));


        LocaleUtils.updateConfig(this, getBaseContext().getResources().getConfiguration());
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LocaleUtils.updateConfig(this, newConfig);
    }

}
