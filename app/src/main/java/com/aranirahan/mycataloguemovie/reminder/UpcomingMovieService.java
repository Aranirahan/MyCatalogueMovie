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
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingMovieService extends GcmTaskService {

    public static String UPCOMING_SCHEDULER_TAG = "UPCOMING_SCHEDULER_TAG";

    private Call<UpcomingItem> call;
    private ApiClient apiClient = new ApiClient();

    @Override
    public int onRunTask(TaskParams taskParams) {
        int result = 0;
        if (Objects.equals(taskParams.getTag(), UPCOMING_SCHEDULER_TAG)) {

            call = apiClient.getService().getUpcomingMovie(MyLocaleState.getLocaleState());
            call.enqueue(new Callback<UpcomingItem>() {
                @Override
                public void onResponse(Call<UpcomingItem> call, Response<UpcomingItem> response) {
                    if (response.isSuccessful()) {
                        List<ResultsItem> listResulsItem = response.body().getResults();
                        int index = new Random().nextInt(listResulsItem.size());

                        String originalTitle = listResulsItem.get(index).getTitle();
                        int notifId = 200;
                        ResultsItem resultsItem = listResulsItem.get(index);

                        showNotificationUpcomigMovie(originalTitle, notifId, resultsItem);

                    } else {
                        Toast.makeText(getApplicationContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UpcomingItem> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), R.string.error_message, Toast.LENGTH_SHORT).show();
                }
            });

            result = GcmNetworkManager.RESULT_SUCCESS;
        }

        return result;
    }

    @Override
    public void onInitializeTasks() {
        super.onInitializeTasks();
        SchedulerTask schedulersTask = new SchedulerTask(this);
        schedulersTask.createPeriodicTask();
    }

    private void showNotificationUpcomigMovie(String originalTitle, int notifId, ResultsItem resultsItem) {
        Context context = getApplicationContext();
        String message = "Today " + originalTitle + " Release";
        NotificationManager notificationManagerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intentDetailActivity = new Intent(context, DetailActivity.class);
        intentDetailActivity.putExtra(DetailActivity.KEY_ITEM, new Gson().toJson(resultsItem));
        PendingIntent pendingIntentDetailActivity = PendingIntent.getActivity(
                context,
                notifId,
                intentDetailActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle(originalTitle)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.white))
                .setContentIntent(pendingIntentDetailActivity)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        notificationManagerCompat.notify(notifId, builder.build());
    }
}
