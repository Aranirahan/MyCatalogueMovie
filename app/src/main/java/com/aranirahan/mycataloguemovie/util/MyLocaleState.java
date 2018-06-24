package com.aranirahan.mycataloguemovie.util;

import java.util.Locale;

public class MyLocaleState {
    public static String getLocaleState() {
        String localeState = Locale.getDefault().getCountry().toLowerCase();
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
