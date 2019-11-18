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
import com.alfara.themoviedbapiwithviewmodel.activity.DetailTvActivity;
import com.alfara.themoviedbapiwithviewmodel.entity.Tv;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterTv extends RecyclerView.Adapter<AdapterTv.MyViewHolder> {
    public static final String DATA_TV = "datatv";
    public static final String DATA_TV_EXTRA = "datatvextra";
    private Context context;
    private ArrayList<Tv> tvs = new ArrayList<>();

    public AdapterTv(Context context, ArrayList<Tv> tvs) {
        this.context = context;
        this.tvs = tvs;
    }

    public ArrayList<Tv> getTvs() {
        return tvs;
    }

    public void setTvs(ArrayList<Tv> tvs) {
        this.tvs.clear();
        this.tvs.addAll(tvs);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AdapterTv.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_tv, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvTitle.setText(tvs.get(position).getOriginalName());
        Glide.with(context).load("https://image.tmdb.org/t/p/w500"+tvs.get(position).getPosterPath()).into(holder.tvPoster);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailTvActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(DATA_TV, Parcels.wrap(tvs.get(position)));
                intent.putExtra(DATA_TV_EXTRA, bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tvs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private ImageView tvPoster;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_item_tv_title);
            tvPoster = itemView.findViewById(R.id.iv_item_tv_poster);
        }
    }
}
