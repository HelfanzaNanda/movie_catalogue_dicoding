package com.alfara.themoviedbapiwithviewmodel.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.adapter.AdapterTv;
import com.alfara.themoviedbapiwithviewmodel.entity.Tv;
import com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract;
import com.bumptech.glide.Glide;
import org.parceler.Parcels;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.COLUMN_ID;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.COLUMN_PLOT_SYNOPSIS;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.COLUMN_POSTER_PATH;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.COLUMN_TITLE;
import static com.alfara.themoviedbapiwithviewmodel.helper.FavoriteContract.FavoriteTvshowEntry.CONTENT_URI_TVSHOW;

public class DetailTvActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvOverview;
    private ImageView ivPoster;
    private Button fav;
    private String title, poster, overview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tv);
        load();
    }

    private boolean isFavorite(String id){
        String selection = FavoriteContract.FavoriteTvshowEntry.COLUMN_ID+" = ?";
        String[] selectionArgs = { id };
        String[] projection = {FavoriteContract.FavoriteTvshowEntry.COLUMN_ID};
        Uri uri = CONTENT_URI_TVSHOW;
        uri = uri.buildUpon().appendPath(id).build();
        Cursor cursor = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            cursor = getContentResolver().query(uri, projection , selection, selectionArgs, null, null);
        }
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        fav.setText(exists ? getString(R.string.remove_fav) : getString(R.string.add_fav) );
        Log.v("isi", Boolean.toString(exists));
        return exists;
    }

    private void load() {
        Bundle bundle = getIntent().getBundleExtra(AdapterTv.DATA_TV_EXTRA);
        Tv tv = Parcels.unwrap(bundle.getParcelable(AdapterTv.DATA_TV));
        fav = findViewById(R.id.favorite_tv);

        title = tv.getOriginalName();
        poster = "https://image.tmdb.org/t/p/w500"+tv.getPosterPath();
        overview = tv.getOverview();


        tvName = findViewById(R.id.tv_item_tv_title_detail);
        tvOverview = findViewById(R.id.tv_item_tv_desc_detail);
        ivPoster = findViewById(R.id.iv_item_tv_poster_detail);

        tvName.setText(title);
        tvOverview.setText(overview);
        Glide.with(DetailTvActivity.this).load(poster).into(ivPoster);

        isFavorite(String.valueOf(tv.getId()));
        setOrRemoveFavorite(tv);
    }

    private void setOrRemoveFavorite(final Tv tv) {
        System.err.println("id is "+tv.getId());

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.err.println("id is "+tv.getId());

                if (!isFavorite(String.valueOf(tv.getId()))){
                    try{
                        System.err.println("id is "+tv.getId());

                        ContentValues values = new ContentValues();
                        values.put(COLUMN_ID, tv.getId());
                        values.put(COLUMN_TITLE, tv.getOriginalName());
                        values.put(COLUMN_POSTER_PATH, tv.getPosterPath());
                        values.put(COLUMN_PLOT_SYNOPSIS, tv.getOverview());
                        getContentResolver().insert(CONTENT_URI_TVSHOW, values);
                        isFavorite(String.valueOf(tv.getId()));
                        Toast.makeText(DetailTvActivity.this, "Berhasil Menambah Favorite", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        System.err.println(e.getMessage());
                        Toast.makeText(DetailTvActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    try{
                        System.err.println("id is "+tv.getId());
                        Uri uri = CONTENT_URI_TVSHOW;
                        uri = uri.buildUpon().appendPath(String.valueOf(tv.getId())).build();
                        getContentResolver().delete(uri, null, null);
                        isFavorite(String.valueOf(tv.getId()));
                        Toast.makeText(DetailTvActivity.this, "Berhasil Menghapus Favorite", Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        System.err.println(e.getMessage());
                        Toast.makeText(DetailTvActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }


}
