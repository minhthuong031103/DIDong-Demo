package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

import com.mobile.moviebooking.Entity.Movie;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> movies = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context)
    {
        this.context=context;
    }
    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.tvName.setText(movie.getName());

    }

    @Override
    public int getItemCount() {
        if (movies != null) {
            return movies.size();
        }
        return 0;
    }

    public void setData(List<Movie> list){

        this.movies = list;
        notifyDataSetChanged();
    }


    class MovieHolder extends RecyclerView.ViewHolder {

private TextView tvName ;
        MovieHolder(@NonNull View itemView) {

            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }

    }

}