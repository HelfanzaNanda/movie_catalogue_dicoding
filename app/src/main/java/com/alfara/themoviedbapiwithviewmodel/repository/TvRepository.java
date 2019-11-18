package com.alfara.themoviedbapiwithviewmodel.repository;

import android.app.Application;
import android.util.Log;

import com.alfara.themoviedbapiwithviewmodel.BuildConfig;
import com.alfara.themoviedbapiwithviewmodel.entity.ResponseTv;
import com.alfara.themoviedbapiwithviewmodel.entity.Tv;
import com.alfara.themoviedbapiwithviewmodel.network.Api;
import com.alfara.themoviedbapiwithviewmodel.network.RetrofitInstance;

import java.util.ArrayList;


import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvRepository {
    private ArrayList<Tv> tvs = new ArrayList<>();
    private MutableLiveData<ArrayList<Tv>> mutableLiveData = new MutableLiveData<>();
    private Application application;

    public TvRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<ArrayList<Tv>> getMutableLiveData() {
        final Api api = RetrofitInstance.getApiService();
        Call<ResponseTv> request = api.getTv(BuildConfig.API_KEY);
        request.enqueue(new Callback<ResponseTv>() {
            @Override
            public void onResponse(Call<ResponseTv> call, Response<ResponseTv> response) {
                ResponseTv tv = response.body();
                if (tv != null && tv.getResults() != null){
                    tvs = tv.getResults();
                    mutableLiveData.setValue(tvs);
                }else {
                    Log.d("Request Not Success", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseTv> call, Throwable t) {
                Log.d("Request OnFailure ", t.getMessage());
            }
        });
        return mutableLiveData;
    }
}
