package com.alfara.themoviedbapiwithviewmodel.helper;

import android.database.Cursor;


import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.alfara.themoviedbapiwithviewmodel.entity.Tv;

import java.util.ArrayList;

public class MovieMappingHelper {
    public static ArrayList<Movie> moviesMapCursorToArrayList(Cursor moviesCursor){
        ArrayList<Movie> moviesList = new ArrayList<>();
        while (moviesCursor.moveToNext()){
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_ID));
            String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE));
            String poster_path = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH));
            String overview = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS));
            moviesList.add(new Movie(id, title, poster_path, overview));
        }

        return moviesList;
    }

    public static Movie moviesMapCursorToObject(Cursor moviesCursor){
        moviesCursor.moveToFirst();
        int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_ID));
        String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE));
        String poster_path = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH));
        String overview = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(FavoriteContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS));
        return new Movie(id, title, poster_path, overview);
    }

}
