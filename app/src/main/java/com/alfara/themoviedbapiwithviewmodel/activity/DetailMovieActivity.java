package com.alfara.themoviedbapiwithviewmodel.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.adapter.AdapterMovie;
import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract;
import com.bumptech.glide.Glide;
import org.parceler.Parcels;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_ID;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_PLOT_SYNOPSIS;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_POSTER_PATH;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.COLUMN_TITLE;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteMovieEntry.CONTENT_URI_MOVIE;

public class DetailMovieActivity extends AppCompatActivity {

    private TextView movieDetailTitle;
    private TextView movieDetailDesc;
    private ImageView movieDetailPoster;
    private Button fav;
    private String title, poster, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        loadData();
    }

    private boolean isFavorite(String id) {
        String selection = FavoriteContract.FavoriteMovieEntry.COLUMN_ID+" = ?";
        String[] selectionArgs = { id };
        String[] projection = {FavoriteContract.FavoriteMovieEntry.COLUMN_ID};
        Uri uri = CONTENT_URI_MOVIE;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cursor = getContentResolver().query(uri, projection ,
                    selection, selectionArgs, null, null);
        }
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        fav.setText(exists ? getString(R.string.remove_fav) : getString(R.string.add_fav) );
        Log.v("isi", Boolean.toString(exists));
        return exists;
    }

    private void loadData(){
        Bundle bundle = getIntent().getBundleExtra(AdapterMovie.DATA_MOVIE_EXTRA);
        Movie movie = Parcels.unwrap(bundle.getParcelable(AdapterMovie.DATA_MOVIE));
        fav = findViewById(R.id.favorite_movie);
        title = movie.getTitle();
        overview = movie.getOverview();
        poster = "https://image.tmdb.org/t/p/w500"+movie.getPosterPath();

        movieDetailTitle = findViewById(R.id.tv_item_movies_title_detail);
        movieDetailDesc = findViewById(R.id.tv_item_movies_desc_detail);
        movieDetailPoster = findViewById(R.id.iv_item_movies_poster_detail);

        movieDetailTitle.setText(title);
        movieDetailDesc.setText(overview);
        Glide.with(DetailMovieActivity.this).load(poster).into(movieDetailPoster);

        isFavorite(String.valueOf(movie.getId()));
        setOrRemoveFavorite(movie);
    }

    private void setOrRemoveFavorite(final Movie movie){
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFavorite(String.valueOf(movie.getId()))){
                    ContentValues values = new ContentValues();
                    values.put(COLUMN_ID, movie.getId());
                    values.put(COLUMN_TITLE, movie.getTitle());
                    values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
                    values.put(COLUMN_PLOT_SYNOPSIS, movie.getOverview());
                    getContentResolver().insert(CONTENT_URI_MOVIE, values);
                    isFavorite(String.valueOf(movie.getId()));
                    Toast.makeText(DetailMovieActivity.this, "Berhasil Menambah Favorite", Toast.LENGTH_SHORT).show();
                }else {
                    Uri uri = CONTENT_URI_MOVIE;
                    uri = uri.buildUpon().appendPath(String.valueOf(movie.getId())).build();
                    getContentResolver().delete(uri, null, null);
                    isFavorite(String.valueOf(movie.getId()));
                    Toast.makeText(DetailMovieActivity.this, "Berhasil Menghapus Favorite", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
