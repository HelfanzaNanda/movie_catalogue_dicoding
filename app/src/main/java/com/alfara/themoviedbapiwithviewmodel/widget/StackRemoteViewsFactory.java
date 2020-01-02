package com.alfara.themoviedbapiwithviewmodel.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;
import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract;
import com.alfara.themoviedbapiwithviewmodel.tab.MovieFavTab;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor cursor;
    int mAppWidgetId;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    private Movie getFavorite(int position){
        if (!cursor.moveToPosition(position)){
            throw new IllegalStateException("position Invalid!");
        }

        return new Movie(
                cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH)),
                cursor.getString(cursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS))
        );
    }
    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        if (cursor != null){
            cursor.close();
        }
        final long identityToken = Binder.clearCallingIdentity();

        cursor = context.getContentResolver().query(FavoriteContract.FavoriteMovieEntry.CONTENT_URI_MOVIE,
                null, null, null, null);
        Log.d("Cursor ", String.valueOf(cursor));
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {
        if (cursor !=null){
            cursor.close();
        }
    }

    @Override
    public int getCount() {
        return cursor == null ? 0 : cursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Movie movie = getFavorite(position);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Log.d("poster widget ", movie.getPosterPath());
        Log.d("poster title", movie.getTitle());

        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context)
                    .asBitmap()
                    .load("https://image.tmdb.org/t/p/w500"+movie.getPosterPath())
                    .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
            rv.setImageViewBitmap(R.id.widget_image, bitmap);
            rv.setTextViewText(R.id.widget_title, movie.getTitle());
        }catch (InterruptedException | ExecutionException e){
            Log.d("Failed to load widget", e.getMessage());
            Toast.makeText(context, "Failed to load Widget "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Bundle extras = new Bundle();
        extras.putInt(FavoriteWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        rv.setOnClickFillInIntent(R.id.widget_image, fillInIntent);
        return rv;
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
    public long getItemId(int position) {
        return cursor.moveToPosition(position) ? cursor.getLong(0) : position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
