package com.alfara.themoviedbapiwithviewmodel.network;

import com.alfara.themoviedbapiwithviewmodel.entity.ResponseMovie;
import com.alfara.themoviedbapiwithviewmodel.entity.ResponseTv;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {

    @GET("movie/upcoming")
    Call<ResponseMovie> getMovie(@Query("api_key") String apikey);

    @GET("search/movie")
    Call<ResponseMovie> searchMovie(@Query("api_key") String apikey, @Query("query") String query);

    @GET("tv/top_rated")
    Call<ResponseTv> getTv(@Query("api_key") String apikey);

    @GET("search/tv")
    Call<ResponseTv> searchTv(@Query("api_key") String apikey, @Query("query") String query);

}
