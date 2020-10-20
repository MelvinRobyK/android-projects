package com.example.todoremainder;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new PreferenceFragment())
                .commit();
    }
}
