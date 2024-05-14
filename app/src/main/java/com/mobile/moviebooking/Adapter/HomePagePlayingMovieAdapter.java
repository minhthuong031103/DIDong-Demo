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

public class HomePagePlayingMovieAdapter extends RecyclerView.Adapter<HomePagePlayingMovieAdapter.ViewHolder> {
    private List<Movie> movieList;
    private Context context;

    public HomePagePlayingMovieAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playing_movie_homepage, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        Glide.with(context).load(movie.getPoster()).into(holder.movieImage);
        holder.movieName.setText(movie.getName());
        holder.movieRating.setText(String.valueOf(movie.getRating()));
        holder.Description.setText(movie.getDuration() + " • " +movie.getGenre());
        holder.movieNumOfRating.setText(String.valueOf(movie.getRatingCount()));

        holder.fullLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetail.class);
            intent.putExtra("movieId", movie.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView movieImage;
        private TextView movieName;
        private TextView movieRating;
        private TextView Description;
        private TextView movieNumOfRating;
        private ConstraintLayout fullLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.poster);
            movieName = itemView.findViewById(R.id.viewPagerTitle);
            movieRating = itemView.findViewById(R.id.viewPagerRating);
            Description = itemView.findViewById(R.id.viewPagerDescrption);
            movieNumOfRating = itemView.findViewById(R.id.viewPagerRatingCount);
            fullLayout = itemView.findViewById(R.id.fullLayout);
        }
    }
}
