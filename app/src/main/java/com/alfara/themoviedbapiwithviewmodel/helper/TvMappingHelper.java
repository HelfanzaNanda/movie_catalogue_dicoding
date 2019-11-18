package com.alfara.themoviedbapiwithviewmodel.helper;

import android.database.Cursor;

import com.alfara.themoviedbapiwithviewmodel.entity.Tv;

import java.util.ArrayList;

public class TvMappingHelper {
    public static ArrayList<Tv> tvsMapCursorToArrayList(Cursor tvsCursor){
        ArrayList<Tv> tvsList = new ArrayList<>();
        while (tvsCursor.moveToNext()){
            int id = tvsCursor.getInt(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_ID));
            String title = tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_TITLE));
            String poster_path = tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_POSTER_PATH));
            String overview = tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_PLOT_SYNOPSIS));
            tvsList.add(new Tv(id, title, poster_path, overview));
        }
        return tvsList;
    }

    public static Tv tvsMapCursorToObject(Cursor tvsCursor){
        tvsCursor.moveToFirst();
        int id = tvsCursor.getInt(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_ID));
        String title = tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_TITLE));
        String poster_path = tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_POSTER_PATH));
        String overview = tvsCursor.getString(tvsCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteTvshowEntry.COLUMN_PLOT_SYNOPSIS));
        return new Tv(id, title, poster_path, overview);
    }
}
