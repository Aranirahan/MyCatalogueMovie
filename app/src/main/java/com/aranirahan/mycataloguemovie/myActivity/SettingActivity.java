package com.aranirahan.mycataloguemovie.myActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.reminder.AlarmReceiver;
import com.aranirahan.mycataloguemovie.reminder.SchedulerTask;
import butterknife.BindString;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static class MyPreferenceFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {


        private AlarmReceiver alarmReceiver = new AlarmReceiver();
        private SchedulerTask schedulerTask;

        @BindString(R.string.key_reminder_daily)
        String reminder_daily;

        @BindString(R.string.key_reminder_upcoming)
        String reminder_upcoming;

        @BindString(R.string.key_setting_locale)
        String setting_locale;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            ButterKnife.bind(this, getActivity());

            findPreference(reminder_daily).setOnPreferenceChangeListener(this);
            findPreference(reminder_upcoming).setOnPreferenceChangeListener(this);
            findPreference(setting_locale).setOnPreferenceClickListener(this);

            schedulerTask = new SchedulerTask(getActivity());
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String key = preference.getKey();
            boolean isOn = (boolean) o;

            if (key.equals(reminder_daily)) {
                if (isOn) {
                    alarmReceiver.setRepeatingAlarm(getActivity(), alarmReceiver.TYPE_REPEATING,
                            "08:14", getString(R.string.label_alarm_daily_reminder));
                } else {
                    alarmReceiver.cancelAlarm(getActivity(), alarmReceiver.TYPE_REPEATING);
                }

                Toast.makeText(getContext(), getString(R.string.label_daily_reminder) + " "
                        + (isOn ? getString(R.string.label_activated) : getString(R.string.label_deactivated)),
                        Toast.LENGTH_SHORT).show();
                return true;
            }

            if (key.equals(reminder_upcoming)) {
                if (isOn) {
                    schedulerTask.createPeriodicTask();
                } else schedulerTask.cancelPeriodicTask();

                Toast.makeText(getContext(), getString(R.string.label_upcoming_reminder) + " "
                        + (isOn ? getString(R.string.label_activated) : getString(R.string.label_deactivated)), Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            String key = preference.getKey();

            if (key.equals(setting_locale)) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return true;
            }

            return false;
        }
    }
}