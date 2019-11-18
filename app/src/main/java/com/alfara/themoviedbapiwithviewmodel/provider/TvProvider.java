package com.alfara.themoviedbapiwithviewmodel.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import com.alfara.themoviedbapiwithviewmodel.helper.TvFavHelper;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.AUTHORITY_TVSHOW;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.CONTENT_URI_TVSHOW;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.TABLE_NAME;


public class TvProvider extends ContentProvider {
    private static final int TV = 1;
    private static final int TV_ID = 2;
    private TvFavHelper tvFavHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY_TVSHOW, TABLE_NAME, TV);

        sUriMatcher.addURI(AUTHORITY_TVSHOW, TABLE_NAME + "/#", TV_ID);
    }

    @Override
    public boolean onCreate() {
        tvFavHelper = TvFavHelper.getInstance(getContext());
        tvFavHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case TV :
                cursor = tvFavHelper.queryAll();
                break;

            case TV_ID :
                cursor = tvFavHelper.queryById(uri.getLastPathSegment());
                break;

             default:
                 cursor = null;
                 break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)){
            case TV :
                added = tvFavHelper.addFavoriteTv(values);
                break;

            default:
                added = 0;
                break;
        }
        if (added > 0){
            getContext().getContentResolver().notifyChange(CONTENT_URI_TVSHOW, null);
        }
        return Uri.parse(CONTENT_URI_TVSHOW+"/"+added);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int updated = 0;
        switch (sUriMatcher.match(uri)){
            case TV_ID:
                updated = tvFavHelper.update(uri.getLastPathSegment(), values);
                break;

            default:
                updated = 0;
                break;
        }

        if (updated > 0 ){
            getContext().getContentResolver().notifyChange(CONTENT_URI_TVSHOW, null);
        }
        return updated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)){
            case TV_ID :
                deleted = tvFavHelper.removeFavoriteTv(uri.getLastPathSegment());
                break;

            default:
                deleted = 0;
        }
        if (deleted > 0 ){
            getContext().getContentResolver().notifyChange(CONTENT_URI_TVSHOW, null);
        }
        return deleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
