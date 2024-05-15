package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.moviebooking.Activity.MovieDetail;
import com.mobile.moviebooking.Entity.Movie;
import com.mobile.moviebooking.R;

import java.util.List;

public class HomePageComingMovieAdapter extends RecyclerView.Adapter<HomePageComingMovieAdapter.HomePageComingMovieHolder>{

    private Context context;
    private List<Movie> movies;

    public HomePageComingMovieAdapter(List<Movie> movies, Context context) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override
    public HomePageComingMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comingsoon_movie_homepage, parent, false);
        return new HomePageComingMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePageComingMovieAdapter.HomePageComingMovieHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.name.setText(movie.getName());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.genre.setText(movie.getGenre());
        Glide.with(context).load(movie.getPoster()).into(holder.poster);
        holder.fullLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetail.class);
            intent.putExtra("movieId", movie.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class HomePageComingMovieHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView releaseDate;
        private TextView genre;
        private ImageView poster;
        private ConstraintLayout fullLayout;

        public HomePageComingMovieHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            releaseDate = itemView.findViewById(R.id.date);
            poster = itemView.findViewById(R.id.poster);
            genre = itemView.findViewById(R.id.genre);
            fullLayout = itemView.findViewById(R.id.fullLayout);
        }
    }
}
