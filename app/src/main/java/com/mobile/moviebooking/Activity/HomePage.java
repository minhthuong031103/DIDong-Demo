package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.apollographql.apollo3.api.ApolloResponse;
import com.apollographql.apollo3.runtime.java.ApolloCallback;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.bumptech.glide.Glide;
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
    private SearchView searchView;
    private ConstraintLayout SearchedMovieLayout;
    private NestedScrollView scrollView;
    private TextView notFound;
    private TextView seeAllPlaying;
    private ImageView seeAllPlayingArrow;
    private TextView seeAllComing;
    private ImageView seeAllComingArrow;
    private ConstraintLayout loginLayout;
    private TextView welcomeText;
    private TextView welcomebackText;
    private SwipeRefreshLayout swipeRefreshLayout;

    private void findViewById() {
        viewPager = findViewById(R.id.viewPager2);
        indicator = findViewById(R.id.indicator);
        comingMovieRecyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchview);
        SearchedMovieLayout = findViewById(R.id.searched_movie);
        scrollView = findViewById(R.id.scrollView);
        notFound = findViewById(R.id.notfound);
        seeAllComing = findViewById(R.id.seeallcomingsoon);
        seeAllComingArrow = findViewById(R.id.imgcomingsoon);
        seeAllPlaying = findViewById(R.id.textView13);
        seeAllPlayingArrow = findViewById(R.id.imageView10);
        loginLayout = findViewById(R.id.loginLayout);
        welcomeText = findViewById(R.id.welcomeText);
        welcomebackText = findViewById(R.id.textView11);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
    }
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
        swipeRefreshLayout.setRefreshing(true);

        setupViewPager();

        loadData();

        setupSearchView();

        setupSeeAllOnClick();

        checkIfLoggedIn();

        navBar();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            playingMovieList.clear();
            comingMovieList.clear();
            loadData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void checkIfLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        if (!sharedPreferences.getString("token", "").isEmpty()) {
            loginLayout.setVisibility(ConstraintLayout.INVISIBLE);
            loginLayout.setClickable(false);
            welcomeText.setText("Hi, "+sharedPreferences.getString("userName", "")+" \uD83D\uDC4B");
            welcomebackText.setVisibility(TextView.VISIBLE);
            welcomeText.setVisibility(TextView.VISIBLE);
        } else {
            loginLayout.setOnClickListener(v -> {
                startActivity(new Intent(HomePage.this, MainActivity.class));
            });
        }
    }

    private void setupSeeAllOnClick() {

        seeAllPlaying.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MovieActivity.class);
            intent.putExtra("title", "Now Playing");
            startActivity(intent);
        });
        seeAllPlayingArrow.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MovieActivity.class);
            intent.putExtra("title", "Now Playing");
            startActivity(intent);
        });

        seeAllComing.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MovieActivity.class);
            intent.putExtra("title", "Coming Soon");
            startActivity(intent);
        });
        seeAllComingArrow.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, MovieActivity.class);
            intent.putExtra("title", "Coming Soon");
            startActivity(intent);
        });
    }

    private void setupSearchView() {
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    SearchedMovieLayout.setVisibility(ConstraintLayout.GONE);
                    scrollView.setVisibility(ScrollView.VISIBLE);
                } else {
                    Movie movie = null;
                    for (Movie playingMovie : playingMovieList) {
                        if (playingMovie.getName().toLowerCase().contains(newText.toLowerCase())) {
                            movie = playingMovie;
                            break;
                        }
                    }
                    if (movie == null) {
                        for (Movie comingMovie : comingMovieList) {
                            if (comingMovie.getName().toLowerCase().contains(newText.toLowerCase())) {
                                movie = comingMovie;
                                break;
                            }
                        }
                    }
                    if (movie != null) {
                        SearchedMovieLayout.setVisibility(ConstraintLayout.VISIBLE);
                        Glide.with(HomePage.this).load(movie.getPoster()).into((ImageView) findViewById(R.id.searched_poster));
                        ((TextView) findViewById(R.id.searched_name)).setText(movie.getName());
                        Movie finalMovie = movie;
                        SearchedMovieLayout.setOnClickListener(v -> {
                            Intent intent = new Intent(HomePage.this, MovieDetail.class);
                            intent.putExtra("movieId", finalMovie.getId());
                            startActivity(intent);
                        });
                        scrollView.setVisibility(ScrollView.INVISIBLE);
                        notFound.setVisibility(TextView.GONE);
                    } else {
                        SearchedMovieLayout.setVisibility(ConstraintLayout.GONE);
                        notFound.setVisibility(TextView.VISIBLE);
                    }
                }
                return false;
            }
        });

        View closeButton = searchView.findViewById(androidx.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            if (!searchView.isIconified())
                searchView.setIconified(true);
            else
                searchView.setQuery("", false);
            notFound.setVisibility(TextView.GONE);
        });
    }

    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(3);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.85f + r * 0.15f);
        });
        viewPager.setPageTransformer(compositePageTransformer);
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
                    swipeRefreshLayout.setRefreshing(false);
                });
            }

        });
    }


    private void navBar() {
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setBarBackgroundColor(R.color.black);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED_NO_TITLE);
        bottomNavigationBar.setActiveColor(R.color.yello_theme);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.home_lh, "Home"))
                .addItem(new BottomNavigationItem(R.drawable.ticket_lh, "Tickets"))
                .addItem(new BottomNavigationItem(R.drawable.movie_lh, "Movie"))
                .addItem(new BottomNavigationItem(R.drawable.profile_lh, "Profile"))
                .setFirstSelectedPosition(0)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(HomePage.this, HomePage.class));
                        break;
                    case 1:
                        startActivity(new Intent(HomePage.this, MyTicket.class));
                        break;
                    case 2:
                        startActivity(new Intent(HomePage.this, MovieActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(HomePage.this, Profile.class));
                        break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }
}