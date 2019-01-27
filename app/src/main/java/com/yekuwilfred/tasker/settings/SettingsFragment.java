package com.yekuwilfred.tasker.settings;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.yekuwilfred.tasker.R;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_tasker);
    }
}
