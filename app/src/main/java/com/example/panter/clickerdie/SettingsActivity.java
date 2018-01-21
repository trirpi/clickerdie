package com.example.panter.clickerdie;

import android.os.Bundle;

public class SettingsActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
