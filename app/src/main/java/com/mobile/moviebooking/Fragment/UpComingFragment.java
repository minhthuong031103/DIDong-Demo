package com.mobile.moviebooking.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.moviebooking.Adapter.MovieAdapter;
import com.mobile.moviebooking.Entity.Movie;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class UpComingFragment extends Fragment {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    List<Movie> listMovie = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_up_coming, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.rvMovie);

        // create movie instances
        Movie movie1 = new Movie();
        movie1.setName("Movie 3");
        listMovie.add(movie1);

        Movie movie2 = new Movie();
        movie2.setName("Movie 4");
        listMovie.add(movie2);


        movieAdapter =new MovieAdapter(getContext());
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        movieAdapter.setData(listMovie);
        recyclerView.setAdapter(movieAdapter);

        super.onViewCreated(view, savedInstanceState);
    }
}