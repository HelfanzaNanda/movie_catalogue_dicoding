package com.alfara.themoviedbapiwithviewmodel.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.TABLE_NAME;

public class MovieFavHelper {
    private static final String DATABASE_TABLE = TABLE_NAME;
    private static FavoriteDbHelper dataBaseHelper;
    private static MovieFavHelper INSTANCE;

    private static SQLiteDatabase database;

    private MovieFavHelper(Context context){
        dataBaseHelper = new FavoriteDbHelper(context);
    }

    public static MovieFavHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MovieFavHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open()throws SQLException {
        database = dataBaseHelper.getWritableDatabase();
    }

    public void close(){
        dataBaseHelper.close();
        if (database.isOpen()){
            database.close();
        }
    }

    public Cursor queryAll(){
        return database.query(DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                FavoriteContract.FavoriteMovieEntry.COLUMN_ID + " DESC");
    }

    public Cursor queryById(String id){
        return database.query(DATABASE_TABLE, null
                , FavoriteContract.FavoriteMovieEntry.COLUMN_ID + " = ?"
                , new String[]{id}
                ,null
                ,null
                ,null
                ,null);
    }

    public long addFavoriteMovie(ContentValues values){
        return database.insert(DATABASE_TABLE, null, values);
    }

    public int update(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values,
                FavoriteContract.FavoriteMovieEntry.COLUMN_ID + " = ?", new String[]{id});
    }

    public int removeFavoriteMovie(String id){
        return database.delete(DATABASE_TABLE, FavoriteContract.FavoriteMovieEntry.COLUMN_ID + " =?", new String[]{id});
    }

}
