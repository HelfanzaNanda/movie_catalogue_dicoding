package com.alfara.themoviedbapiwithviewmodel.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alfara.themoviedbapiwithviewmodel.R;
import com.alfara.themoviedbapiwithviewmodel.activity.DetailMovieActivity;
import com.alfara.themoviedbapiwithviewmodel.entity.Movie;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMovie extends RecyclerView.Adapter<AdapterMovie.MyViewHolder> {
    public static final String DATA_MOVIE = "datamovie";
    public static final String DATA_MOVIE_EXTRA = "datamovieextra";
    private Context context;
    private ArrayList<Movie> movies = new ArrayList<>();

    public AdapterMovie(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.moviesTitle.setText(movies.get(position).getTitle());
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+movies.get(position).getPosterPath()).into(holder.moviesPoster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMovieActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(DATA_MOVIE, Parcels.wrap(movies.get(position)));
                intent.putExtra(DATA_MOVIE_EXTRA, bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView moviesTitle;
        ImageView moviesPoster;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            moviesTitle = itemView.findViewById(R.id.tv_item_movies_title);
            moviesPoster = itemView.findViewById(R.id.iv_item_movies_poster);
        }
    }
}
