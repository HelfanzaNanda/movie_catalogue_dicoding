package com.alfara.themoviedbapiwithviewmodel.adapter;

import android.content.Context;

import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.tab.MovieFavTab;
import com.alfara.themoviedbapiwithviewmodel.tab.TvFavTab;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public SectionsPagerAdapter( FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        fragments.add(new MovieFavTab());
        fragments.add(new TvFavTab());
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{R.string.movie_fav, R.string.tv_fav};

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position){
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
