package com.example.todoremainder;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

public class ApplicationStart extends Application {

    private static final String TAG = "ApplicationStart";
    public static boolean isDarkModeOn;
    public static int signUpFlag = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Of Application is called before any other activities or components ");
        setBackground();
    }

    private void setBackground() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isDarkModeOn = sharedPreferences.getBoolean("dark_mode", false);
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
