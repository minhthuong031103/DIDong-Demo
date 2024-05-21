package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.mobile.moviebooking.Activity.MovieDetail;
import com.mobile.moviebooking.Entity.Movie;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class MovieUpcomingAdapter extends RecyclerView.Adapter<MovieUpcomingAdapter.MovieUpcomingHolder> {

    private List<Movie> movieupcomings = new ArrayList<>();
    private Context context;

    public MovieUpcomingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieUpcomingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.up_coming_item, parent, false);
        return new MovieUpcomingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieUpcomingHolder holder, int position) {
        Movie movieupcoming = movieupcomings.get(position);

        holder.tvName.setText(movieupcoming.getName());
        holder.tvGenre.setText(movieupcoming.getGenre());
        holder.tvReleaseDate.setText(movieupcoming.getReleaseDate());
        Glide.with(context).load(movieupcoming.getPoster()).into(holder.imvPoster);


        holder.cvFilm.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetail.class);
            intent.putExtra("movieId", movieupcoming.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        if (movieupcomings != null) {
            return movieupcomings.size();
        }
        return 0;
    }

    public void setData(List<Movie> list) {
        this.movieupcomings = list;
        notifyDataSetChanged();
    }


    class MovieUpcomingHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvGenre;
        private TextView tvReleaseDate;
        private ImageView imvPoster;
        private CardView cvFilm;
        MovieUpcomingHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvNameUp);
            tvGenre = itemView.findViewById(R.id.tvGenresUp);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDateUp);
            imvPoster = itemView.findViewById(R.id.imvPosterUp);
            cvFilm = itemView.findViewById(R.id.cvFilmUp);
        }

    }

}