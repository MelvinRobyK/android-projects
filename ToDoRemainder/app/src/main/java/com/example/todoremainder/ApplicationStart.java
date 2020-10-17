package com.example.todoremainder;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

public class ApplicationStart extends Application {

    private static final String TAG = "ApplicationStart" ;
    public static boolean isDarkModeOn;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: Of Application is called before any other activities or components ");
        setBackground();
    }

    private void setBackground() {
        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
