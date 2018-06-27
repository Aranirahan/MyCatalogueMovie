/*
 * Created by omrobbie.
 * Copyright (c) 2018. All rights reserved.
 * Last modified 11/11/17 3:14 PM.
 */

package com.aranirahan.mycataloguemovie.reminder;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.api.ApiClient;
import com.aranirahan.mycataloguemovie.model.main.UpcomingItem;
import com.aranirahan.mycataloguemovie.model.sub.ResultsItem;
import com.aranirahan.mycataloguemovie.myActivity.DetailActivity;
import com.aranirahan.mycataloguemovie.util.MyLocaleState;
import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.TaskParams;
import com.google.gson.Gson;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SchedulerService extends GcmTaskService {

    public static String TAG_TASK_UPCOMING = "upcoming movies";

    private Call<UpcomingItem> apiCall;
    private ApiClient apiClient = new ApiClient();

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (taskParams.getTag().equals(TAG_TASK_UPCOMING)) {
            loadData();
            result = GcmNetworkManager.RESULT_SUCCESS;
        }

        return result;
    }

    private void loadData() {
        apiCall = apiClient.getService().getUpcomingMovie(MyLocaleState.getLocaleState());
        apiCall.enqueue(new Callback<UpcomingItem>() {
            @Override
            public void onResponse(Call<UpcomingItem> call, Response<UpcomingItem> response) {
                if (response.isSuccessful()) {
                    List<ResultsItem> items = response.body().getResults();
                    int index = new Random().nextInt(items.size());

                    ResultsItem item = items.get(index);
                    String title = items.get(index).getTitle();
                    String message = items.get(index).getOverview();
                    int notifId = 200;

                    showNotification(getApplicationContext(), title, message, notifId, item);

                } else loadFailed();
            }

            @Override
            public void onFailure(Call<UpcomingItem> call, Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    private void showNotification(Context context, String title, String message, int notifId, ResultsItem item) {
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_ITEM, new Gson().toJson(item));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }
}
