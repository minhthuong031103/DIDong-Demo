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

public class MovieNowPlayingAdapter extends RecyclerView.Adapter<MovieNowPlayingAdapter.MovieNowPlayingHolder> {

    private List<Movie> movienowplayings = new ArrayList<>();
    private Context context;

    public MovieNowPlayingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MovieNowPlayingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.now_playing_item, parent, false);
        return new MovieNowPlayingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieNowPlayingHolder holder, int position) {
        Movie movienowplaying = movienowplayings.get(position);

        holder.tvName.setText(movienowplaying.getName());
        holder.tvGenre.setText(movienowplaying.getGenre());
        holder.tvRating.setText(String.valueOf(movienowplaying.getRating()));
        holder.tvRatingCount.setText("(" + String.valueOf(movienowplaying.getRatingCount()) + ")");
        Glide.with(context).load(movienowplaying.getPoster()).into(holder.imvPoster);

        holder.cvFilm.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetail.class);
            intent.putExtra("movieId", movienowplaying.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (movienowplayings != null) {
            return movienowplayings.size();
        }
        return 0;
    }

    public void setData(List<Movie> list) {
        this.movienowplayings = list;
        notifyDataSetChanged();
    }


    class MovieNowPlayingHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvRating;
        private TextView tvRatingCount;
        private TextView tvGenre;
        private ImageView imvPoster;
        private CardView cvFilm;
        MovieNowPlayingHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvNameNow);
            tvRating = (TextView) itemView.findViewById(R.id.tvRatingNow);
            tvRatingCount = (TextView) itemView.findViewById(R.id.tvRatingCountNow);
            imvPoster = (ImageView) itemView.findViewById(R.id.imvPosterNow);
            tvGenre = (TextView) itemView.findViewById(R.id.tvGenresNow);

            cvFilm = (CardView) itemView.findViewById(R.id.cvFilmNow);
        }


    }

}