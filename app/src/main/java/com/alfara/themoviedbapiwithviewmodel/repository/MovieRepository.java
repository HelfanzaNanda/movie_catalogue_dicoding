package com.alfara.themoviedbapiwithviewmodel.repository;

import android.app.Application;
import android.util.Log;
import android.view.ViewParent;
import android.widget.Toast;

import com.alfara.themoviedbapiwithviewmodel.BuildConfig;
import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.alfara.themoviedbapiwithviewmodel.entity.ResponseMovie;
import com.alfara.themoviedbapiwithviewmodel.network.Api;
import com.alfara.themoviedbapiwithviewmodel.network.RetrofitInstance;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private ArrayList<Movie> movies = new ArrayList<>();
    private MutableLiveData<ArrayList<Movie>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public MovieRepository(Application application){
        this.application = application;
    }

    public MutableLiveData<ArrayList<Movie>> getMutableLiveData(){
        final Api api = RetrofitInstance.getApiService();
        Call<ResponseMovie> call = api.getMovie(BuildConfig.API_KEY);
        call.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                ResponseMovie movie = response.body();
                if (movie != null && movie.getResults() != null){
                    movies = movie.getResults();
                    mutableLiveData.setValue(movies);
                }else {
                    Toast.makeText(application.getApplicationContext(), "Request Not SUccess "+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {
                Toast.makeText(application.getApplicationContext(), "Request onFailure"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return mutableLiveData;
    }
}
