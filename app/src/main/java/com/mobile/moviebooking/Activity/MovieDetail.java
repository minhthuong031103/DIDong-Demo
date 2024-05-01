package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mobile.moviebooking.Adapter.CelebAdapter;
import com.mobile.moviebooking.Entity.Celeb;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieDetail extends AppCompatActivity {
    private ImageView backBtn;
    RecyclerView directorRecyclerView, actorRecyclerView;
    List<Celeb> directorList = new ArrayList<>();
    List<Celeb> actorList = new ArrayList<>();
    ExtendedFloatingActionButton bookingBtn;
    int movieId;
    String movieName;
    String moviePoster;
    String movieTrailer;
    String movieDescription;
    String movieRating;
    int numberOfRatings;
    int movieDuration;
    String movieGenre;
    Date movieReleaseDate;
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
        loadDirector();
        loadActor();

        bookingBtn = findViewById(R.id.btn);
        bookingBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MovieDetail.this, SelectShowtime.class);
            startActivity(intent);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("movieId", movieId);
            editor.putString("movieName", movieName);
            editor.putString("moviePoster", moviePoster);
            editor.apply();
        });

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void loadMovieDetails() {
        movieId = 2;
        movieName = "Avenger: Endgame";
        moviePoster = "https://upload.wikimedia.org/wikipedia/vi/2/2d/Avengers_Endgame_bia_teaser.jpg";
    }

    private void findViewById() {
        backBtn = findViewById(R.id.backBtn);
        directorRecyclerView = findViewById(R.id.directorRecyclerView);
    }

    private void loadDirector() {

        directorList.add(new Celeb("James", "Cameron","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0000116/"));
        directorList.add(new Celeb("Christopher", "Nolan","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0634240/"));
        directorList.add(new Celeb("Steven", "Spielberg","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0000229/"));
        directorList.add(new Celeb("Quentin", "Tarantino","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0000233/"));

        directorRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        CelebAdapter directorAdapter = new CelebAdapter(this, directorList);
        directorRecyclerView.setAdapter(directorAdapter);
    }

    private void loadActor() {
        actorList.add(new Celeb("Leonardo", "DiCaprio","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0000138/"));
        actorList.add(new Celeb("Tom", "Hanks","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0000158/"));
        actorList.add(new Celeb("Robert", "De Niro","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0000134/"));
        actorList.add(new Celeb("Brad", "Pitt","https://firebasestorage.googleapis.com/v0/b/crescentmoon-a4dbd.appspot.com/o/James_Cameron_by_Gage_Skidmore.jpg?alt=media&token=12465718-9713-4279-ac61-769ed85a3697","https://www.imdb.com/name/nm0000093/"));
        actorRecyclerView = findViewById(R.id.actorRecyclerView);
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