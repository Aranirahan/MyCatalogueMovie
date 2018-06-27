package com.aranirahan.mycataloguemovie.reminder;

import android.content.Context;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;


public class SchedulerTask {
    private GcmNetworkManager mGcmNetworkManager;

    public SchedulerTask(Context context) {
        mGcmNetworkManager = GcmNetworkManager.getInstance(context);
    }

    public void createPeriodicTask() {
        Task periodicTask = new PeriodicTask.Builder()
                .setService(UpcomingMovieService.class)
                .setPeriod(30)
                .setFlex(10)
                .setTag(UpcomingMovieService.UPCOMING_SCHEDULER_TAG)
                .setPersisted(true)
                .build();
        mGcmNetworkManager.schedule(periodicTask);
    }

    public void cancelPeriodicTask() {
        if (mGcmNetworkManager != null) {
            mGcmNetworkManager.cancelTask(
                    UpcomingMovieService.UPCOMING_SCHEDULER_TAG,
                    UpcomingMovieService.class);
        }
    }
}
