package com.alfara.themoviedbapiwithviewmodel.fragment;

import android.content.Context;
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

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.alfara.themoviedbapiwithviewmodel.BuildConfig;
import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.adapter.AdapterMovie;
import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.alfara.themoviedbapiwithviewmodel.entity.ResponseMovie;
import com.alfara.themoviedbapiwithviewmodel.network.Api;
import com.alfara.themoviedbapiwithviewmodel.network.RetrofitInstance;
import com.alfara.themoviedbapiwithviewmodel.viewmodel.ViewModelMovie;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFragment extends Fragment implements OnQueryTextListener {

    private ArrayList<Movie> movies = new ArrayList<>();
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private ViewModelMovie viewModelMovie;
    private AdapterMovie adapter;
    private SearchView searchView;
    private IBinder binder;
    private InputMethodManager inputManager;

    //private static final String EXTRA_STATE = "EXTRA_STATE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);
        progressBar = view.findViewById(R.id.pgBarMovie);
        recycler = view.findViewById(R.id.rv_movies);
        binder = view.getWindowToken();
        inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        load();
        return view;
    }

    private void load(){
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new AdapterMovie(getActivity(), movies);
        adapter.notifyDataSetChanged();
        recycler.setAdapter(adapter);
        viewModelMovie = ViewModelProviders.of(getActivity()).get(ViewModelMovie.class);
        getMovie();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private void getMovie() {
        viewModelMovie.getAllMovie().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(ArrayList<Movie> resultsItems) {
                prepareRecyclerView(resultsItems);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void prepareRecyclerView(ArrayList<Movie> resultsItems){
        adapter = new AdapterMovie(getActivity(), resultsItems);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) { return false; }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (!newText.isEmpty()){
            searchMovie(newText);
        } else {
            hideKeyboard();
            getMovie();
        }
        return true;
    }

    private void searchMovie(String query) {
        progressBar.setVisibility(View.VISIBLE);
        final Api api = RetrofitInstance.getApiService();
        Call<ResponseMovie> call = api.searchMovie(BuildConfig.API_KEY, query);
        call.enqueue(new Callback<ResponseMovie>() {
            @Override
            public void onResponse(Call<ResponseMovie> call, Response<ResponseMovie> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    movies = response.body().getResults();
                    recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recycler.setAdapter(new AdapterMovie(getActivity(), movies));

                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseMovie> call, Throwable t) {
                //Toast.makeText(getActivity(), "Request onFailure "+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.search_movies));
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void hideKeyboard(){
        inputManager.hideSoftInputFromWindow(binder,
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
