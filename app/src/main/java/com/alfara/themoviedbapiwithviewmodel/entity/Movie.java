package com.alfara.themoviedbapiwithviewmodel.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_ID;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE;

public class Movie implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    public Movie(int id, String title, String posterPath, String overview) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public Movie() {

    }

    public Movie(long movieid, String movietitle, String poster, String overview) {

    }

/*    public Movie(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
        this.title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
        this.posterPath = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSTER_PATH));
    }*/

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    public Movie(Parcel in) {
        overview = in.readString();
        title = in.readString();
        posterPath = in.readString();
        id = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(overview);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
