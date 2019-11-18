package com.alfara.themoviedbapiwithviewmodel.viewmodel;


import android.app.Application;

import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.alfara.themoviedbapiwithviewmodel.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModelMovie extends AndroidViewModel {
    private MovieRepository movieRepository;

    public ViewModelMovie(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
    }

    public LiveData<ArrayList<Movie>> getAllMovie(){ return movieRepository.getMutableLiveData(); }
}
