package org.softeg.morphinebrowser;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/*
 * Created by slartus on 25.10.2014.
 */
public class AppPreferences {
    public static int getWebViewFontSize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        return prefs.getInt("WebView.FontSize", 15);
    }

    public static void setWebViewFontSize(int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(App.getInstance());
        prefs.edit().putInt("WebView.FontSize", value).apply();
    }

}
