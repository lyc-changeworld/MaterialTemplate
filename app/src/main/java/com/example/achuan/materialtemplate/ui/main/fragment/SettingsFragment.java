package com.example.achuan.materialtemplate.ui.main.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.example.achuan.materialtemplate.R;

/**
 * Created by achuan on 17-2-15.
 */

public  class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }

}
