package com.alfara.themoviedbapiwithviewmodel;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.alfara.themoviedbapiwithviewmodel.activity.NotificationSettingActivity;
import com.alfara.themoviedbapiwithviewmodel.fragment.FavFragment;
import com.alfara.themoviedbapiwithviewmodel.fragment.MovieFragment;
import com.alfara.themoviedbapiwithviewmodel.fragment.TvShowFragment;
import com.alfara.themoviedbapiwithviewmodel.helper.FavoriteDbHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_movies:
                    //toast("movies clicked");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, new MovieFragment(), getString(R.string.movies))
                            .commit();
                    break;
                case R.id.navigation_tvshow:
                    //toast("tvshow clicked");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, new TvShowFragment(), getString(R.string.tv_show)).commit();
                    break;

                case R.id.navigation_fav:
                    //toast("fav clicked");
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_layout, new FavFragment(), getString(R.string.favorite)).commit();
                    break;

                default:
                     //toast("movies clicked");
                     getSupportFragmentManager().beginTransaction()
                             .replace(R.id.container_layout, new MovieFragment(), getString(R.string.movies)).commit();

            }
            return false;
        }
    };



    /*private void toast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_movies);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_ubah_bahasa) {
//            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
//            startActivity(mIntent);
//        }
        switch (item.getItemId()) {
            case R.id.action_ubah_bahasa:
                Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(mIntent);
                break;
            case R.id.action_ubah_notif:
                Intent intent = new Intent(MainActivity.this, NotificationSettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
