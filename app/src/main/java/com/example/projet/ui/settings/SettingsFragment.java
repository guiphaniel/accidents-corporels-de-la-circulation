package com.example.projet.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.projet.R;


public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    EditTextPreference etDepCode;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);

        etDepCode = findPreference("dep_code");
        etDepCode.setSummary(etDepCode.getText());

        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("dep_code")) {
            String dep = etDepCode.getText();
            if(!dep.matches( "\\b([013-8]\\d?|2[aAbB1-9]?|9[0-59]?|97[1-6])\\b"))
                etDepCode.setText("01");

            etDepCode.setSummary(etDepCode.getText().length() == 1 ? "0" + etDepCode.getText() : etDepCode.getText());
        }

    }
}