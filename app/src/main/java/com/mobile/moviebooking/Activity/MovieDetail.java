package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo3.api.Optional;

import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.bumptech.glide.Glide;
import com.example.rocketreserver.MovieDetailsQuery;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.mobile.moviebooking.Adapter.CelebAdapter;
import com.mobile.moviebooking.Entity.Celeb;
import com.mobile.moviebooking.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import okhttp3.OkHttpClient;

import okhttp3.logging.HttpLoggingInterceptor;

public class MovieDetail extends AppCompatActivity {
    private ImageView backBtn;
    SimpleRatingBar ratingBar;
    RecyclerView directorRecyclerView, actorRecyclerView;
    List<Celeb> directorList = new ArrayList<>();
    List<Celeb> actorList = new ArrayList<>();
    ExtendedFloatingActionButton bookingBtn;
    ImageView poster;
    Optional<String> movieId = Optional.present("1");
    TextView tv_movieName;
    String movieName;
    String moviePoster;
    MaterialCardView trailer;
    String movieTrailer;
    TextView tv_movieDescription;
    String movieDescription;
    TextView tv_rating;
    String movieRating;
    TextView tv_numOfRatings;
    int numberOfRatings;
    int movieDuration;
    Date movieReleaseDate;
    TextView tv_durationAndDateRelease;
    TextView tv_genre;
    TextView tv_censorship;
    TextView tv_language;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById();
        sharedPreferences = getSharedPreferences("movieInfo", MODE_PRIVATE);

        loadMovieDetails();

        setupEventListener();

    }

    private void setupEventListener() {
        bookingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MovieDetail.this, SelectShowtime.class);
            startActivity(intent);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("movieId", String.valueOf(movieId));
            editor.putString("movieName", movieName);
            editor.putString("moviePoster", moviePoster);
            editor.apply();
        });

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        trailer.setOnClickListener(v -> {
            String url = movieTrailer;
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            this.startActivity(intent);
        });
    }

    private void loadMovieDetails() {
        String Token = "Bearer " +
                "893a13fa53a2ff80efe5b37c1fd5942434971882b53655b0542e4ccdb7ab76bbd28fbbac96939f04f01bdb1c098492f91d908e8dc38b3092f348bf2d190ffa91354f451a38afadd4063f6fcbb256e84a7b9ad7e7c8775be390ba32a68d2c393bca77d6a2031dfd3358a9760dad48ca115b7086103cd355c140aa99451fd510c0";
        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", Token)
                .build();

        apolloClient.query(new MovieDetailsQuery(movieId)).enqueue(response -> {
            if (response.data != null) {

                runOnUiThread(() -> {
                    moviePoster = response.data.movie.data.attributes.poster.data.attributes.url;
                    Glide.with(MovieDetail.this)
                        .load(moviePoster)
                        .into(poster);
                    tv_rating.setText(response.data.movie.data.attributes.review.toString());
                    tv_numOfRatings.setText("("+response.data.movie.data.attributes.num_of_reviews+")");
                    movieName = response.data.movie.data.attributes.title;
                    tv_movieName.setText(movieName);
                    tv_movieDescription.setText(response.data.movie.data.attributes.description);
                    movieTrailer = response.data.movie.data.attributes.trailer.data.attributes.url;
                    ratingBar.setRating((response.data.movie.data.attributes.review).floatValue());
                    int iDuration = response.data.movie.data.attributes.duration;

                    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = null;
                    try {
                        date = (Date)formatter.parse(response.data.movie.data.attributes.release_date.toString());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    tv_durationAndDateRelease.setText(
                            iDuration/60+"h"+(iDuration%60)+"m • "+new SimpleDateFormat("dd.MM.yyyy").format(date));

                    String movieGenre = "";
                    List<MovieDetailsQuery.Data3> movie_genres = response.data.movie.data.attributes.movie_genres.data;
                    for (MovieDetailsQuery.Data3 genre: movie_genres) {
                        movieGenre += genre.attributes.name + ", ";
                    }
                    tv_genre.setText(movieGenre.substring(0, movieGenre.length()-2));
                    tv_censorship.setText(response.data.movie.data.attributes.label.rawValue);
                    tv_language.setText(response.data.movie.data.attributes.language.rawValue);

                    loadDirector(response.data.movie.data.attributes.directors.data);
                    loadActor(response.data.movie.data.attributes.actors.data);

                });
            } else {
                Log.e("error", "error: "+response.exception);
            }
        });
    }

    private void findViewById() {
        backBtn = findViewById(R.id.backBtn);
        directorRecyclerView = findViewById(R.id.directorRecyclerView);
        actorRecyclerView = findViewById(R.id.actorRecyclerView);
        poster = findViewById(R.id.imageView);
        tv_movieDescription = findViewById(R.id.readMoreTextView);
        tv_movieName = findViewById(R.id.movieName);
        tv_rating = findViewById(R.id.rating);
        tv_numOfRatings = findViewById(R.id.num_rating);
        tv_durationAndDateRelease = findViewById(R.id.info);
        ratingBar = findViewById(R.id.ratingBar);
        trailer = findViewById(R.id.trailer);
        bookingBtn = findViewById(R.id.btn);
        tv_genre = findViewById(R.id.tv_movie_genre);
        tv_censorship = findViewById(R.id.tv_censorship);
        tv_language = findViewById(R.id.tv_language);
    }

    private void loadDirector(List<MovieDetailsQuery.Data7> directors) {
        directorList.clear();
        for (MovieDetailsQuery.Data7 director: directors) {
            Map<String, Map<String, Object>> avatarFormats = (Map<String, Map<String, Object>>) director.attributes.avatar.data.attributes.formats;
            Map<String, Object> largeFormat = avatarFormats.get("thumbnail");
            String avatarUrl = (String) largeFormat.get("url");
            directorList.add(new Celeb(
                    director.attributes.name.substring(0, director.attributes.name.indexOf(" ")),
                    director.attributes.name.substring(director.attributes.name.indexOf(" ")).trim(),
                    avatarUrl,
                    director.attributes.infoUrl));
        }
        directorRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CelebAdapter directorAdapter = new CelebAdapter(this, directorList);
        directorRecyclerView.setAdapter(directorAdapter);
    }

    private void loadActor(List<MovieDetailsQuery.Data5> actors) {
        actorList.clear();
        for (MovieDetailsQuery.Data5 actor: actors) {
            Map<String, Map<String, Object>> avatarFormats = (Map<String, Map<String, Object>>) actor.attributes.avatar.data.attributes.formats;
            Log.d("avatarFormats", avatarFormats.toString());
            Map<String, Object> largeFormat = avatarFormats.get("thumbnail");
            String avatarUrl = (String) largeFormat.get("url");
            actorList.add(new Celeb(
                    actor.attributes.name.substring(0, actor.attributes.name.indexOf(" ")),
                    actor.attributes.name.substring(actor.attributes.name.indexOf(" ")).trim(),
                    avatarUrl,
                    actor.attributes.infoUrl));
        }
        actorRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CelebAdapter actorAdapter = new CelebAdapter(this, actorList);
        actorRecyclerView.setAdapter(actorAdapter);
    }

    @Override
    protected void onDestroy() {
        sharedPreferences.edit().clear().apply();
        super.onDestroy();
    }
}