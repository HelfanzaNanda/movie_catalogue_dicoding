package com.alfara.themoviedbapiwithviewmodel.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorite.db";
    private static final int DATABASE_VERSION = 1;
    public static final String LOGTAG = "FAVORITE";

    SQLiteOpenHelper dbhandler;
    SQLiteDatabase db;


    private static final String SQL_CREATE_TABLE_MOVIE_FAVORITE = String.format("CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " UNIQUE(%s) ON CONFLICT REPLACE)",
            FavoriteContract.FavoriteMovieEntry.TABLE_NAME,
            FavoriteContract.FavoriteMovieEntry.COLUMN_ID,
            FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE,
            FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH,
            FavoriteContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS,
            FavoriteContract.FavoriteMovieEntry.COLUMN_ID
    );

    private static final String SQL_CREATE_TABLE_TV_FAVORITE = String.format("CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " UNIQUE(%s) ON CONFLICT REPLACE)",
            FavoriteContract.FavoriteTvshowEntry.TABLE_NAME,
            FavoriteContract.FavoriteTvshowEntry.COLUMN_ID,
            FavoriteContract.FavoriteTvshowEntry.COLUMN_TITLE,
            FavoriteContract.FavoriteTvshowEntry.COLUMN_POSTER_PATH,
            FavoriteContract.FavoriteTvshowEntry.COLUMN_PLOT_SYNOPSIS,
            FavoriteContract.FavoriteTvshowEntry.COLUMN_ID
    );


    public FavoriteDbHelper(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    public void open(){
        Log.i(LOGTAG, "Database Opened");
        db = dbhandler.getWritableDatabase();
    }

    public void close(){
        Log.i(LOGTAG, "Database Closed");
        dbhandler.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_MOVIE_FAVORITE);
        db.execSQL(SQL_CREATE_TABLE_TV_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.FavoriteTvshowEntry.TABLE_NAME);
        onCreate(db);
    }


    /*public void addFavTvshow(com.alfara.themoviedbapiwithviewmodel.response_tv.ResultsItem tvshow){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.FavoriteTvshowEntry.COLUMN_ID, tvshow.getId());
        values.put(FavoriteContract.FavoriteTvshowEntry.COLUMN_TITLE, tvshow.getOriginalName());
        values.put(FavoriteContract.FavoriteTvshowEntry.COLUMN_POSTER_PATH, tvshow.getPosterPath());
        values.put(FavoriteContract.FavoriteTvshowEntry.COLUMN_PLOT_SYNOPSIS, tvshow.getOverview());
        db.insert(FavoriteContract.FavoriteTvshowEntry.TABLE_NAME, null, values);
        db.close();
    }
    public void removeFavTvshow(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(FavoriteContract.FavoriteTvshowEntry.TABLE_NAME,
                FavoriteContract.FavoriteTvshowEntry.COLUMN_ID+"="+id, null);
    }

    public List<com.alfara.themoviedbapiwithviewmodel.response_tv.ResultsItem> getAllFavTvshow(){
        String[] columns = {
                FavoriteContract.FavoriteTvshowEntry._ID,
                FavoriteContract.FavoriteTvshowEntry.COLUMN_ID,
                FavoriteContract.FavoriteTvshowEntry.COLUMN_TITLE,
                FavoriteContract.FavoriteTvshowEntry.COLUMN_POSTER_PATH,
                FavoriteContract.FavoriteTvshowEntry.COLUMN_PLOT_SYNOPSIS
        };
        String sortOrder = FavoriteContract.FavoriteTvshowEntry._ID + " ASC";
        List<com.alfara.themoviedbapiwithviewmodel.response_tv.ResultsItem> favTvshowList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(FavoriteContract.FavoriteTvshowEntry.TABLE_NAME,
                columns, null, null, null, null, sortOrder
        );

        if (cursor.moveToFirst()){
            do {
                com.alfara.themoviedbapiwithviewmodel.response_tv.ResultsItem tvshow = new com.alfara.themoviedbapiwithviewmodel.response_tv.ResultsItem();
                tvshow.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteTvshowEntry.COLUMN_ID))));
                tvshow.setOriginalName(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteTvshowEntry.COLUMN_TITLE)));
                tvshow.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteTvshowEntry.COLUMN_POSTER_PATH)));
                tvshow.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteContract.FavoriteTvshowEntry.COLUMN_PLOT_SYNOPSIS)));
                favTvshowList.add(tvshow);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return favTvshowList;
    }*/
}
