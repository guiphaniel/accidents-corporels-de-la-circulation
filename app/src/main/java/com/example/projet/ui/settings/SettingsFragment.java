package com.example.projet.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projet.MainActivity;
import com.example.projet.R;
import com.example.projet.databinding.FragmentListBinding;
import com.example.projet.databinding.FragmentSettingsBinding;
import com.example.projet.model.Accident;
import com.example.projet.model.SharedModel;
import com.example.projet.ui.list.ListViewModel;

import java.util.ArrayList;


public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    EditTextPreference etDep;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);

        etDep = findPreference("dep");
        etDep.setSummary(etDep.getText());

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
        if(key.equals("dep")) {
            String dep = etDep.getText();
            if(!dep.matches( "\\b([013-8]\\d?|2[aAbB1-9]?|9[0-59]?|97[1-6])\\b"))
                etDep.setText("01");

            etDep.setSummary(etDep.getText());
        }

    }
}