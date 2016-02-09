package com.aware.plugin.cmu_esm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.aware.Aware;

public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * Activate/deactivate plugin
     */
    public static final String STATUS_PLUGIN_CMU_ESM = "status_plugin_cmu_esm";

    private CheckBoxPreference status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        status = (CheckBoxPreference) findPreference(STATUS_PLUGIN_CMU_ESM);
        if( Aware.getSetting(this, STATUS_PLUGIN_CMU_ESM).length() == 0 ) {
            Aware.setSetting(this, STATUS_PLUGIN_CMU_ESM, true);
        }
        status.setChecked(Aware.getSetting(this, STATUS_PLUGIN_CMU_ESM).equals("true"));

        Intent fetchQuestions = new Intent(this, QuestionUpdater.class);
        startService(fetchQuestions);

        Aware.startPlugin(this, "com.aware.plugin.cmu_esm");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if( key.equalsIgnoreCase(STATUS_PLUGIN_CMU_ESM) ) {
            if( sharedPreferences.getBoolean(key, false) ) {
                Aware.setSetting(this, key, true);
                Aware.startPlugin(this, "com.aware.plugin.cmu_esm");
            } else {
                Aware.setSetting(this, key, false);
                Aware.stopPlugin(this, "com.aware.plugin.cmu_esm");
            }
        }
    }
}
