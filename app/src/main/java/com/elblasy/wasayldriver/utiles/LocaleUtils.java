package com.elblasy.wasayldriver.utiles;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.ContextThemeWrapper;


import java.util.Locale;

public class LocaleUtils {

    public static final String LAN_ARABIC = "ar";
    public static final String LAN_ENGLISH = "en";

    private static Locale sLocale;

    public static void setLocale(Locale locale) {
        sLocale = locale;
        if (sLocale != null) {
            Locale.setDefault(sLocale);
        }
    }

    public static void updateConfig(ContextThemeWrapper wrapper) {

        Configuration configuration = new Configuration();
        configuration.setLocale(sLocale);
        wrapper.applyOverrideConfiguration(configuration);

    }

    public static void updateConfig(Application app, Configuration configuration) {

        //Wrapping the configuration to avoid Activity endless loop
        Configuration config = new Configuration(configuration);
        config.locale = sLocale;
        config.setLayoutDirection(sLocale);
        Resources res = app.getBaseContext().getResources();
        res.updateConfiguration(config, res.getDisplayMetrics());

    }


}

