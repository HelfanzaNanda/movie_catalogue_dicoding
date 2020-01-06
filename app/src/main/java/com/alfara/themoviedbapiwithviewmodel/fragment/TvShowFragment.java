package com.alfara.themoviedbapiwithviewmodel.fragment;


import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.alfara.themoviedbapiwithviewmodel.BuildConfig;
import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.adapter.AdapterTv;
import com.alfara.themoviedbapiwithviewmodel.entity.ResponseTv;
import com.alfara.themoviedbapiwithviewmodel.entity.Tv;
import com.alfara.themoviedbapiwithviewmodel.network.Api;
import com.alfara.themoviedbapiwithviewmodel.network.RetrofitInstance;
import com.alfara.themoviedbapiwithviewmodel.viewmodel.ViewModelTv;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFragment extends Fragment implements SearchView.OnQueryTextListener{

    private ArrayList<Tv> tvs = new ArrayList<>();
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private AdapterTv adapter;
    private ViewModelTv viewModelTv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_show, container, false);
        recycler = view.findViewById(R.id.rv_tv);
        progressBar = view.findViewById(R.id.pgBarTv);
        load();
        return view;
    }

    private void load(){
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterTv(getActivity(), tvs);
        adapter.notifyDataSetChanged();
        recycler.setAdapter(adapter);

        viewModelTv = ViewModelProviders.of(getActivity()).get(ViewModelTv.class);
        getTv();
    }

    private void getTv() {
        viewModelTv.getPopularTv().observe(this, new Observer<ArrayList<Tv>>() {
            @Override
            public void onChanged(ArrayList<Tv> resultsItems) {
                prepareRecyclerView(resultsItems);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void prepareRecyclerView(ArrayList<Tv> resultsItems){
        adapter = new AdapterTv(getActivity(), resultsItems);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void searchTv(String query) {
        progressBar.setVisibility(View.VISIBLE);
        final Api api = RetrofitInstance.getApiService();
        Call<ResponseTv> call = api.searchTv(BuildConfig.API_KEY, query);
        call.enqueue(new Callback<ResponseTv>() {
            @Override
            public void onResponse(Call<ResponseTv> call, Response<ResponseTv> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    tvs = response.body().getResults();
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recycler.setAdapter(new AdapterTv(getActivity(), tvs));
                }else {
                    //Toast.makeText(getActivity(), "Request not Success "+response.message(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseTv> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "Request onFalure "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchTv(newText);
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
         inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.search_tv));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
        getTv();
    }
}
