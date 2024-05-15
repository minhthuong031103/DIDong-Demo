package com.mobile.moviebooking.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.runtime.java.ApolloCallback;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.MovieDetailsQuery;
import com.example.rocketreserver.MovieHomaePageQuery;
import com.mobile.moviebooking.Adapter.HomePageComingMovieAdapter;
import com.mobile.moviebooking.Adapter.HomePagePlayingMovieAdapter;
import com.mobile.moviebooking.Entity.Movie;
import com.mobile.moviebooking.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class HomePage extends AppCompatActivity {

    private ViewPager2 viewPager;
    private HomePagePlayingMovieAdapter homepagePlayingMovieAdapter;
    private List<Movie> playingMovieList = new ArrayList<Movie>();
    private List<Movie> comingMovieList = new ArrayList<Movie>();
    private RecyclerView comingMovieRecyclerView;
    private HomePageComingMovieAdapter homepageComingMovieAdapter;
    private CircleIndicator3 indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById();

        viewPager.setOffscreenPageLimit(3);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPager.setPageTransformer(compositePageTransformer);

        loadData();
    }

    private void loadData() {
        String Token = "Bearer " +
                "893a13fa53a2ff80efe5b37c1fd5942434971882b53655b0542e4ccdb7ab76bbd28fbbac96939f04f01bdb1c098492f91d908e8dc38b3092f348bf2d190ffa91354f451a38afadd4063f6fcbb256e84a7b9ad7e7c8775be390ba32a68d2c393bca77d6a2031dfd3358a9760dad48ca115b7086103cd355c140aa99451fd510c0";
        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", Token)
                .build();
        apolloClient.query(new MovieHomaePageQuery()).enqueue(apolloResponse -> {
            List<MovieHomaePageQuery.Data1> Movies = apolloResponse.data.movies.data;
            for (MovieHomaePageQuery.Data1 Movie : Movies) {
                Movie movie = new Movie();
                movie.setId(Movie.id);
                movie.setName(Movie.attributes.title);
                movie.setPoster(Movie.attributes.poster.data.attributes.url);
                movie.setRating(Movie.attributes.review);
                movie.setRatingCount(Movie.attributes.num_of_reviews);

                String movieGenre = "";
                List<MovieHomaePageQuery.Data3> movie_genres = Movie.attributes.movie_genres.data;
                for (MovieHomaePageQuery.Data3 genre: movie_genres) {
                    movieGenre += genre.attributes.name + ", ";
                }
                movie.setGenre(movieGenre.substring(0, movieGenre.length()-2));

                int iDuration = Movie.attributes.duration;
                movie.setDuration(iDuration/60+"h"+(iDuration%60)+"m");

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date date = null;
                try {
                    date = (Date)formatter.parse(Movie.attributes.release_date.toString());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                movie.setReleaseDate(new SimpleDateFormat("dd.MM.yyyy").format(date));

                movie.setPlaying(Movie.attributes.is_showing);
                if (movie.isPlaying()) {
                    playingMovieList.add(movie);
                } else {
                    comingMovieList.add(movie);
                }
                runOnUiThread(() -> {
                    homepagePlayingMovieAdapter = new HomePagePlayingMovieAdapter(playingMovieList, HomePage.this);
                    viewPager.setAdapter(homepagePlayingMovieAdapter);
                    indicator.setViewPager(viewPager);

                    comingMovieRecyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false));
                    homepageComingMovieAdapter = new HomePageComingMovieAdapter(comingMovieList, HomePage.this);
                    comingMovieRecyclerView.setAdapter(homepageComingMovieAdapter);
                });
            }

        });
    }

    private void findViewById() {
        viewPager = findViewById(R.id.viewPager2);
        indicator = findViewById(R.id.indicator);
        comingMovieRecyclerView = findViewById(R.id.recyclerView);
    }
}