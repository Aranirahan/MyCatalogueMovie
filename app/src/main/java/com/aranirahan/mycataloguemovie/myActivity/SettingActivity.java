package com.aranirahan.mycataloguemovie.myActivity;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.reminder.DailyAlarmReceiver;
import com.aranirahan.mycataloguemovie.reminder.SchedulerTask;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new ReminderFragment()).commit();
    }

    public static class ReminderFragment extends PreferenceFragment
            implements Preference.OnPreferenceChangeListener {


        private DailyAlarmReceiver dailyAlarmReceiver;
        SchedulerTask schedulerTask;

        private String reminderDailyKey;
        private String reminderUpcomingKey;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            dailyAlarmReceiver = new DailyAlarmReceiver();
            schedulerTask = new SchedulerTask(getActivity());
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
                    dailyAlarmReceiver.setRepeatingAlarm(getActivity(), "03:47:59");
                } else {
                    dailyAlarmReceiver.cancelAlarm(getActivity());
                }
                return true;
            }

            if (Objects.equals(preferenceKey, reminderUpcomingKey)) {
                if ((boolean) object) {
                    schedulerTask.createPeriodicTask();
                } else schedulerTask.cancelPeriodicTask();

                return true;
            }

            return false;
        }
    }
}