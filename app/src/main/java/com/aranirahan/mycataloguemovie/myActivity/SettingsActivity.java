package com.aranirahan.mycataloguemovie.myActivity;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.remider.AlarmReceiver;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new ReminderFragment()).commit();
    }

    public static class ReminderFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {
        private AlarmReceiver alarmReceiver = new AlarmReceiver();
        private String reminderDailyKey;
        private String reminderUpcomingKey;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            reminderDailyKey = getString(R.string.reminder_daily_key);
            reminderUpcomingKey = getString(R.string.reminder_upcoming_key);

            findPreference(reminderDailyKey).setOnPreferenceChangeListener(this);
            findPreference(reminderUpcomingKey).setOnPreferenceChangeListener(this);

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public boolean onPreferenceChange(Preference preference, Object object) {
            String preferenceKey = preference.getKey();

            if (Objects.equals(preferenceKey, reminderDailyKey)) {
                if ((boolean) object) {
                    alarmReceiver.setDailyAlarm(getActivity(), AlarmReceiver.TYPE_DAILY,
                            "07:00:00", getString(R.string.daily_reminder_message));
                } else {
                    alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_DAILY);
                }
                return true;
            }

            if (Objects.equals(preferenceKey, reminderUpcomingKey)) {
                if ((boolean) object) {
                    alarmReceiver.setUpcomingAlarm(getActivity(), AlarmReceiver.TYPE_UPCOMING,
                            "08:00:00");
                } else {
                    alarmReceiver.cancelAlarm(getActivity(), AlarmReceiver.TYPE_UPCOMING);
                }
                return true;
            }

            return false;

        }

    }
}