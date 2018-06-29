package com.aranirahan.mycataloguemovie.remider;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.api.ApiClient;
import com.aranirahan.mycataloguemovie.model.main.UpcomingItem;
import com.aranirahan.mycataloguemovie.model.sub.ResultsItem;
import com.aranirahan.mycataloguemovie.myActivity.MainActivity;
import com.aranirahan.mycataloguemovie.util.MyLocaleState;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_DAILY = "DailyAlarm";
    public static final String TYPE_UPCOMING = "UpcomingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TYPE = "type";

    private final int NOTIF_ID_DAILY = 100;
    private final int NOTIF_ID_UPCOMING = 101;

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        int notifId = type.equalsIgnoreCase(TYPE_DAILY) ? NOTIF_ID_DAILY : NOTIF_ID_UPCOMING;
        String title = context.getResources().getString(R.string.app_name);

        showAlarmNotification(context, title, message, notifId);
    }

    private void showAlarmNotification(Context context, String title, String message, int notifId) {
        NotificationManager notificationManagerCompat =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(context, notifId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        assert notificationManagerCompat != null;
        notificationManagerCompat.notify(notifId, builder.build());
    }

    public void setDailyAlarm(Context context, String type, String time, String message) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TYPE, type);

        String timeArray[] = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_DAILY, intent, 0);

        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public void setUpcomingAlarm(final Context context, final String type, final String time) {

        ApiClient apiClient = new ApiClient();
        Call<UpcomingItem> call = apiClient.getService().getUpcomingMovie(MyLocaleState.getLocaleState());
        call.enqueue(new Callback<UpcomingItem>() {
            @Override
            public void onResponse(@NonNull Call<UpcomingItem> call,
                                   @NonNull Response<UpcomingItem> response) {
                if (response.isSuccessful()) {
                    List<ResultsItem> listResulsItem = Objects.requireNonNull(response.body()).getResults();
                    int index = new Random().nextInt(listResulsItem.size());

                    String originalTitle = listResulsItem.get(index).getTitle();
                    String message = "Today " + originalTitle + " release";

                    AlarmManager alarmManager =
                            (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    Intent intent = new Intent(context, AlarmReceiver.class);

                    intent.putExtra(EXTRA_MESSAGE, message);
                    intent.putExtra(EXTRA_TYPE, type);

                    String timeArray[] = time.split(":");

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
                    calendar.set(Calendar.SECOND, 0);

                    if (calendar.before(Calendar.getInstance())) calendar.add(Calendar.DATE, 1);

                    PendingIntent pendingIntent =
                            PendingIntent.getBroadcast(
                                    context,
                                    NOTIF_ID_UPCOMING,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);

                    assert alarmManager != null;
                    alarmManager.setInexactRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent);

                } else {
                    Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpcomingItem> call, @NonNull Throwable t) {
                Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        int requestCode = type.equalsIgnoreCase(TYPE_DAILY) ? NOTIF_ID_DAILY : NOTIF_ID_UPCOMING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        assert alarmManager != null;
        alarmManager.cancel(pendingIntent);
    }
}
