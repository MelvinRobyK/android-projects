package com.example.todoremainder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

public class PreferenceFragment extends PreferenceFragmentCompat {

    SwitchPreference switchPreference;
    ListPreference listPreference;
    SharedPreferences sharedPreferences;

    private static final String TAG = "PreferenceFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.main_preference,rootKey);
        switchPreference = findPreference("dark_mode");
        listPreference = findPreference("alarm_preset");
        if(listPreference.getValue() == null)
            listPreference.setValueIndex(0);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if(!switchPreference.isChecked()){
                    Log.i(TAG, "onPreferenceChange: True");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    ApplicationStart.isDarkModeOn = true;
                }
                else{
                    Log.i(TAG, "onPreferenceChange: False");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    ApplicationStart.isDarkModeOn = false;
                }
                return true;
            }
        });
    }
}
