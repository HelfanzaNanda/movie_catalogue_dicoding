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
import com.alfara.themoviedbapiwithviewmodel.adapter.AdapterTv;
import com.alfara.themoviedbapiwithviewmodel.entity.Tv;
import com.alfara.themoviedbapiwithviewmodel.helper.MovieMappingHelper;
import com.alfara.themoviedbapiwithviewmodel.helper.TvMappingHelper;
import com.google.android.material.snackbar.Snackbar;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.CONTENT_URI_TVSHOW;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFavTab extends Fragment implements LoadTvFavCallback{

    private static final String EXTRA_STATE_TV = "EXTRA_STATE_TV";
    private ArrayList<Tv> tvlist;
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private AdapterTv adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tv_fav_tab, container, false);
        progressBar = view.findViewById(R.id.pg_fav_tv);
        recycler = view.findViewById(R.id.rv_fav_tv);
        tvlist = new ArrayList<>();
        adapter = new AdapterTv(getActivity(), tvlist);

        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        HandlerThread handlerThread = new HandlerThread("DataObserverTV");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getActivity(), this);
        getActivity().getContentResolver().registerContentObserver(CONTENT_URI_TVSHOW, true, myObserver);

        if (savedInstanceState == null) {
            new LoadTvAsync(getActivity(), this).execute();
        } else {
            ArrayList<Tv> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE_TV);
            if (list != null) {
                adapter.setTvs(list);
            }
        }
        progressBar.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE_TV, adapter.getTvs());
    }

    private static class LoadTvAsync extends AsyncTask<Void, Void, ArrayList<Tv>> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadTvFavCallback> weakCallback;

        private LoadTvAsync(Context context, LoadTvFavCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected ArrayList<Tv> doInBackground(Void... voids) {
            Context context = weakContext.get();
            Cursor dataCursor = context.getContentResolver()
                    .query(CONTENT_URI_TVSHOW, null, null, null, null);
            return TvMappingHelper.tvsMapCursorToArrayList(dataCursor);
        }

        @Override
        protected void onPostExecute(ArrayList<Tv> tv) {
            super.onPostExecute(tv);
            weakCallback.get().postExecute(tv);
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(recycler, message, Snackbar.LENGTH_SHORT).show();
    }

    public static class DataObserver extends ContentObserver {
        final Context context;
        LoadTvFavCallback callback;

        public DataObserver(Handler handler, Context context, LoadTvFavCallback callback) {
            super(handler);
            this.context = context;
            this.callback = callback;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadTvAsync(context, callback).execute();

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
    public void postExecute(ArrayList<Tv> tvs1) {
        progressBar.setVisibility(View.INVISIBLE);
        if (tvs1.size() > 0) {
            adapter.setTvs(tvs1);
        } else {
            adapter.setTvs(new ArrayList<Tv>());
            //showSnackbarMessage("Tidak ada data saat ini");
        }
    }

}

interface LoadTvFavCallback {
    void preExecute();
    void postExecute(ArrayList<Tv> tvs);
}