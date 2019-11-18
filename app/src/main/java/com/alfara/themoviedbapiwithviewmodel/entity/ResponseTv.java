package com.alfara.themoviedbapiwithviewmodel.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseTv {

    @SerializedName("results")
    private ArrayList<Tv> results;

    public ArrayList<Tv> getResults() {
        return results;
    }

    public void setResults(ArrayList<Tv> results) {
        this.results = results;
    }
}
