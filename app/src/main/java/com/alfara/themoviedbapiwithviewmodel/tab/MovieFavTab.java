package com.alfara.themoviedbapiwithviewmodel.tab;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.adapter.AdapterMovie;
import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract;
import com.alfara.themoviedbapiwithviewmodel.helper.MovieMappingHelper;
import com.google.android.material.snackbar.Snackbar;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.CONTENT_URI_MOVIE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavTab extends Fragment implements LoadMovieFavCallback {

    private static final String EXTRA_STATE_MOVIE = "EXTRA_STATE_MOVIE";
    private ArrayList<Movie> movielist;
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private AdapterMovie adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie_fav_tab, container, false);

        progressBar = view.findViewById(R.id.pg_fav_Movie);
        recycler = view.findViewById(R.id.rv_fav_movies);
        movielist = new ArrayList<>();
        adapter = new AdapterMovie(getActivity(), movielist);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        HandlerThread handlerThread = new HandlerThread("DataObserverMovie");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity(), this);
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI_MOVIE, true, myObserver);

        if (savedInstanceState == null) {
            new LoadMovieAsync(getActivity(), this).execute();
        } else {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE_MOVIE);
            if (list != null) {
                adapter.setMovies(list);
            }
        }
        progressBar.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE_MOVIE, adapter.getMovies());
    }

    private static class LoadMovieAsync extends AsyncTask<Void, Void, ArrayList<Movie>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMovieFavCallback> weakCallback;

        private LoadMovieAsync(Context context, LoadMovieFavCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver()
                    .query(CONTENT_URI_MOVIE, null, null, null, null);
            return MovieMappingHelper.moviesMapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> notes) {
            super.onPostExecute(notes);
            weakCallback.get().postExecute(notes);
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(recycler, message, Snackbar.LENGTH_SHORT).show();
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        LoadMovieFavCallback callback;

        public DataObserver(Handler handler, Context context, LoadMovieFavCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
//            new LoadMovieAsync(context, (LoadMovieFavCallback) context).execute();
            new LoadMovieAsync(context, callback).execute();

        }
    }

    @Override
    public void preExecute() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExecute(ArrayList<Movie> movies1) {
        progressBar.setVisibility(View.INVISIBLE);
        if (movies1.size() > 0) {
            adapter.setMovies(movies1);
        } else {
            adapter.setMovies(new ArrayList<Movie>());
            showSnackbarMessage("Tidak ada data saat ini");
        }
    }

}

interface LoadMovieFavCallback {
    void preExecute();
    void postExecute(ArrayList<Movie> notes);
}
