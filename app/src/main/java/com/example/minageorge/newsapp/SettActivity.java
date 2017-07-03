package com.example.minageorge.newsapp;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sett);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("News Settings");

        Fragment fragment = new settingFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            fragmentTransaction.replace(R.id.pref_content, fragment, "settfrag");
            fragmentTransaction.commit();
        } else {
            fragment = getFragmentManager().findFragmentByTag("settfrag");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class settingFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sett);
            ListPreference dataPref = (ListPreference) findPreference(getResources().getString(R.string.settings_home_key));
            if (dataPref.getValue() == null) {
                dataPref.setValueIndex(0);
            }
            Preference search = findPreference(getString(R.string.settings_home_key));
            bindPreferenceSummaryToValue(search);
        }

        private void bindPreferenceSummaryToValue(Preference search) {
            search.setOnPreferenceChangeListener(this);
            Preference preference;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(search.getContext());
            String preferenceString = preferences.getString(search.getKey(), "");
            onPreferenceChange(search, preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            preference.setSummary("value is : " + stringValue);
            return true;
        }
    }
}
