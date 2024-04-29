package com.mobile.moviebooking.Activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.moviebooking.Adapter.CinemaAdapter;
import com.mobile.moviebooking.Adapter.DateAdapter;
import com.mobile.moviebooking.Entity.Cinema;
import com.mobile.moviebooking.Entity.Showtime;
import com.mobile.moviebooking.R;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class SelectShowtime extends AppCompatActivity {

    private RecyclerView dateRecyclerView;
    private List<Date> dates = new ArrayList<>();
    private ListView cinemaListView;
    private List<Cinema> cinemas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_showtime);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById();
        loadDate();
        loadCinema();
    }

    private void findViewById() {
        dateRecyclerView = findViewById(R.id.rv_date);
        cinemaListView = findViewById(R.id.lv_cinema);
    }

    private void loadCinema(){

        List<Showtime> showtimes = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showtimes.add(new Showtime(6, 60000, Date.from(Instant.parse("2024-02-11T12:30:00.000Z")), 1, 1));
            showtimes.add(new Showtime(1, 60000, Date.from(Instant.parse("2024-02-11T14:45:00.000Z")), 1, 1));
            showtimes.add(new Showtime(2, 60000, Date.from(Instant.parse("2024-02-11T16:45:00.000Z")), 1, 1));
            showtimes.add(new Showtime(3, 60000, Date.from(Instant.parse("2024-02-11T18:45:00.000Z")), 1, 1));
            showtimes.add(new Showtime(4, 60000, Date.from(Instant.parse("2024-02-11T20:00:00.000Z")), 1, 1));
            showtimes.add(new Showtime(5, 60000, Date.from(Instant.parse("2024-02-11T22:30:00.000Z")), 1, 1));
        }

        cinemas.add(new Cinema("Vincom Ocean Park CGV", "Da Ton, Gia Lam, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));
        cinemas.add(new Cinema("Aeon Mall CGV", "27 Co Linh, Long Bien, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));
        cinemas.add(new Cinema("CGV Pamulang Timur", "7-9 Nguyen Van Linh, Long Bien, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));
        cinemas.add(new Cinema("CGV Pamulang Timur", "7-9 Nguyen Van Linh, Long Bien, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));

        CinemaAdapter cinemaAdapter = new CinemaAdapter(this, R.layout.cinema_item, cinemas);
        cinemaListView.setAdapter(cinemaAdapter);
    }

    private void loadDate() {
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 9).getTime());
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 10).getTime());
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime());
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 12).getTime());
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 13).getTime());
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 14).getTime());
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 15).getTime());
        dates.add(new GregorianCalendar(2014, Calendar.FEBRUARY, 16).getTime());

        dateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        DateAdapter dateAdapter = new DateAdapter(this, dates);
        dateRecyclerView.setAdapter(dateAdapter);
    }
}