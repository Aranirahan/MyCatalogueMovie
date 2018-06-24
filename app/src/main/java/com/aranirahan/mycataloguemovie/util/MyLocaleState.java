package com.aranirahan.mycataloguemovie.util;

import android.util.Log;

import java.util.Locale;

public class MyLocaleState {
    public static String getLocaleState() {
        String localeState = Locale.getDefault().getCountry().toLowerCase();
        Log.d("MLS", localeState);
        switch (localeState) {
            case "id":
                break;

            default:
                localeState = "en";
                break;
        }
        return localeState;
    }
}
