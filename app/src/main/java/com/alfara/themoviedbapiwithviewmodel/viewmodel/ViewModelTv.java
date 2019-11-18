package com.alfara.themoviedbapiwithviewmodel.viewmodel;

import android.app.Application;

import com.alfara.themoviedbapiwithviewmodel.entity.Tv;
import com.alfara.themoviedbapiwithviewmodel.repository.TvRepository;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ViewModelTv extends AndroidViewModel {

    private TvRepository tvRepository;

    public ViewModelTv(@NonNull Application application) {
        super(application);
        tvRepository = new TvRepository(application);
    }

    public LiveData<ArrayList<Tv>> getPopularTv(){ return tvRepository.getMutableLiveData(); }
}
