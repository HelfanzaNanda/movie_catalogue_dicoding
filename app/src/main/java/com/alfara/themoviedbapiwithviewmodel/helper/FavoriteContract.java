package com.alfara.themoviedbapiwithviewmodel.helper;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoriteContract {

    public static final String AUTHORITY_MOVIE = "com.alfara.themoviedbapiwithviewmodel.movie";
    public static final String AUTHORITY_TVSHOW = "com.alfara.themoviedbapiwithviewmodel.tvshow";
    private static final String SCHEME = "content";


    public static final class FavoriteMovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";
        public static final Uri CONTENT_URI_MOVIE = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY_MOVIE)
                .appendPath(TABLE_NAME)
                .build();
    }

    public static final class FavoriteTvshowEntry implements BaseColumns{
        public static final String TABLE_NAME = "tvshow";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "posterpath";
        public static final String COLUMN_PLOT_SYNOPSIS = "overview";
        public static final Uri CONTENT_URI_TVSHOW = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY_TVSHOW)
                .appendPath(TABLE_NAME)
                .build();
    }
}