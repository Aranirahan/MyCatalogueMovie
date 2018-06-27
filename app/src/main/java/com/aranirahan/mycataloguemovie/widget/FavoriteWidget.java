package com.aranirahan.mycataloguemovie.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.myActivity.MainActivity;

public class FavoriteWidget extends AppWidgetProvider {

    public static final String TOAST_ACTION = "TOAST_ACTION";
    public static final String EXTRA_ITEM = "EXTRA_ITEM";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, StackWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.favorite_widget);
        remoteViews.setRemoteAdapter(R.id.stack_view, intent);
        remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);

        Intent toastIntent = new Intent(context, FavoriteWidget.class);
        toastIntent.setAction(FavoriteWidget.TOAST_ACTION);
        toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent toastPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                toastIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setPendingIntentTemplate(
                R.id.stack_view,
                toastPendingIntent);
        appWidgetManager.notifyAppWidgetViewDataChanged(
                appWidgetId,
                R.id.stack_view);
        appWidgetManager.updateAppWidget(
                appWidgetId,
                remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            Intent intentMainActivity = new Intent(context, MainActivity.class);
            context.startActivity(intentMainActivity);

        } else if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)){
            ComponentName thisAppWidget = new ComponentName(
                    context.getPackageName(),
                    FavoriteWidget.class.getName()
            );
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            appWidgetManager.notifyAppWidgetViewDataChanged(
                    appWidgetIds,
                    R.id.stack_view
            );
        }
        super.onReceive(context, intent);
    }
}

