package com.mobile.moviebooking.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.MovieHomaePageQuery;
import com.mobile.moviebooking.Activity.HomePage;
import com.mobile.moviebooking.Adapter.HomePageComingMovieAdapter;
import com.mobile.moviebooking.Adapter.HomePagePlayingMovieAdapter;
import com.mobile.moviebooking.Adapter.MovieAdapter;
import com.mobile.moviebooking.Adapter.MovieNowPlayingAdapter;
import com.mobile.moviebooking.Entity.Movie;
import com.mobile.moviebooking.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NowPlayingFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieNowPlayingAdapter movieAdapter;
    List<Movie> listMovie = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        loadData();
        findView(view);


        super.onViewCreated(view, savedInstanceState);
    }

    private void loadData() {
        // create movie instances
//        Movie movie1 = new Movie();
//        movie1.setName("Movie 1");
//        listMovie.add(movie1);
//
//        Movie movie2 = new Movie();
//        movie2.setName("Movie 2");
//        listMovie.add(movie2);

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
                    listMovie.add(movie);
                    for (Movie movie1 : listMovie) {
                        System.out.println(movie1.getName());
                    }
                } else {
                    //comingMovieList.add(movie);
                }

                while(true){
                    if (getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                movieAdapter.setData(listMovie);
                            }
                        });
                        break;
                    }

                }
            }

        });

    }

    private void findView(View view) {

        recyclerView = view.findViewById(R.id.rvMovie);
        movieAdapter =new MovieNowPlayingAdapter(getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter.setData(listMovie);
        recyclerView.setAdapter(movieAdapter);
    }
}