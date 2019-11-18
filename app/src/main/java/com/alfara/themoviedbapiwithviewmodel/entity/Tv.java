package com.alfara.themoviedbapiwithviewmodel.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Tv implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("original_name")
    private String originalName;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    public Tv(int id, String originalName, String posterPath, String overview) {
        this.id = id;
        this.originalName = originalName;
        this.posterPath = posterPath;
        this.overview = overview;
    }

    public Tv(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public static Creator<Tv> getCREATOR() {
        return CREATOR;
    }

    protected Tv(Parcel in) {
        id = in.readInt();
        originalName = in.readString();
        posterPath = in.readString();
        overview = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalName);
        dest.writeString(posterPath);
        dest.writeString(overview);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tv> CREATOR = new Creator<Tv>() {
        @Override
        public Tv createFromParcel(Parcel in) {
            return new Tv(in);
        }

        @Override
        public Tv[] newArray(int size) {
            return new Tv[size];
        }
    };
}
