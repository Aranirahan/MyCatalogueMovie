package com.aranirahan.mycataloguemovie.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.aranirahan.mycataloguemovie.BuildConfig;
import com.aranirahan.mycataloguemovie.R;
import com.aranirahan.mycataloguemovie.model.sub.ResultsItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

import static com.aranirahan.mycataloguemovie.database.DatabaseContract.CONTENT_URI;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int mAppWidgetId;

    private Cursor list;

    public StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        context = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        list = context.getContentResolver().query(
                CONTENT_URI,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return list.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        ResultsItem resultsItem = getResultsItem(position);
        String posterPath = resultsItem.getPosterPath();
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load(BuildConfig.BASE_URL_IMAGE + "w500" + posterPath)
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),
                R.layout.stack_widget_item);
        remoteViews.setImageViewBitmap(R.id.imageView, bitmap);
        remoteViews.setTextViewText(
                R.id.tanggalFavorite,
                resultsItem.getReleaseDate()
        );
        Bundle extras = new Bundle();
        extras.putLong(FavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.imageView, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private ResultsItem getResultsItem(int position) {
        list.moveToPosition(position);
        return new ResultsItem(list);
    }
}
