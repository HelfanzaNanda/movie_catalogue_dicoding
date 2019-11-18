package com.alfara.themoviedbapiwithviewmodel.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import com.alfara.themoviedbapiwithviewmodel.helper.MovieFavHelper;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.AUTHORITY_MOVIE;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.CONTENT_URI_MOVIE;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.TABLE_NAME;

public class MovieProvider extends ContentProvider {

    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private MovieFavHelper movieFavHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY_MOVIE, TABLE_NAME, MOVIE);

        sUriMatcher.addURI(AUTHORITY_MOVIE, TABLE_NAME + "/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        movieFavHelper = MovieFavHelper.getInstance(getContext());
        movieFavHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE :
                cursor = movieFavHelper.queryAll();
                break;

            case MOVIE_ID :
                cursor = movieFavHelper.queryById(uri.getLastPathSegment());
                break;

            default :
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return  null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)){
            case MOVIE :
                added = movieFavHelper.addFavoriteMovie(values);
                break;

            default:
                added = 0;
                break;
        }
        if (added > 0){
            getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
        }
        return Uri.parse(CONTENT_URI_MOVIE+"/"+added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated = 0;
        switch (sUriMatcher.match(uri)){
            case MOVIE_ID :
                movieFavHelper.update(uri.getLastPathSegment(), values);
                break;

             default:
                 updated = 0;
                 break;
        }
        if (updated > 0){
            getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
        }
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)){
            case MOVIE_ID :
                deleted = movieFavHelper.removeFavoriteMovie(uri.getLastPathSegment());
                break;

            default :
                deleted = 0;
                break;
        }

        if (deleted > 0){
            getContext().getContentResolver().notifyChange(CONTENT_URI_MOVIE, null);
        }

        return deleted;
    }
}
