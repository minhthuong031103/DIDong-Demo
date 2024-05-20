package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.GetShowTimesQuery;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mobile.moviebooking.Adapter.CinemaAdapter;
import com.mobile.moviebooking.Adapter.DateAdapter;
import com.mobile.moviebooking.Entity.Cinema;
import com.mobile.moviebooking.Entity.Showtime;
import com.mobile.moviebooking.Interface.IClickItemDateListener;
import com.mobile.moviebooking.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SelectShowtime extends AppCompatActivity {
    private ImageView backBtn;
    private RecyclerView dateRecyclerView;
    private List<Date> dates = new ArrayList<>();
    private ListView cinemaListView;
    private List<Cinema> cinemas = new ArrayList<>();
    private ExtendedFloatingActionButton continueBtn;
    private Intent intent;
    SharedPreferences sharedPreferences;
    List<GetShowTimesQuery.Data1> ListShowTime;
    TextView tv_movieName;
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

        intent = new Intent(this, SelectSeat.class);

        findViewById();

        loadData();


        continueBtn.setOnClickListener(v -> {
            if (intent.getIntExtra("showtimeId", -1) != -1) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select a showtime", Toast.LENGTH_SHORT).show();
            }
        });
        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });


    }

    private void findViewById() {
        backBtn = findViewById(R.id.iv_back);
        dateRecyclerView = findViewById(R.id.rv_date);
        cinemaListView = findViewById(R.id.lv_cinema);
        continueBtn = findViewById(R.id.btn_continue);
        tv_movieName = findViewById(R.id.title);
    }

    private void loadCinema(Date date)  {
        cinemas.clear();
        List<Showtime> showtimes = new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            showtimes.add(new Showtime(6, 60000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-02-11 08:45:00"), 1, 1));
//            showtimes.add(new Showtime(1, 60000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-02-11 16:45:00"), 1, 1));
//            showtimes.add(new Showtime(2, 60000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-02-11 17:45:00"), 1, 1));
//            showtimes.add(new Showtime(3, 60000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-02-11 18:45:00"), 1, 1));
//            showtimes.add(new Showtime(4, 60000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-02-11 19:45:00"), 1, 1));
//            showtimes.add(new Showtime(5, 60000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2024-02-11 20:45:00"), 1, 1));
//        }

//        cinemas.add(new Cinema("Vincom Ocean Park CGV", "Da Ton, Gia Lam, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));
//        cinemas.add(new Cinema("Aeon Mall CGV", "27 Co Linh, Long Bien, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));
//        cinemas.add(new Cinema("CGV Pamulang Timur", "7-9 Nguyen Van Linh, Long Bien, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));
//        cinemas.add(new Cinema("CGV Pamulang Timur", "7-9 Nguyen Van Linh, Long Bien, Ha Noi", "https://res.cloudinary.com/dlglmq9np/image/upload/v1714378297/bh_cinema_logo_npg0ws.png", showtimes));

        for (GetShowTimesQuery.Data1 showtime : ListShowTime) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC+7"));
            Date dateInShowTime = null;
            try {
                dateInShowTime = sdf.parse(showtime.attributes.show_time.toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            if (date.getYear() == dateInShowTime.getYear()
                    && date.getMonth() == dateInShowTime.getMonth()
                    && date.getDate() == dateInShowTime.getDate()){
                Cinema cinema = new Cinema(
                        Integer.parseInt(showtime.attributes.screen.data.attributes.cinema.data.id),
                        showtime.attributes.screen.data.attributes.cinema.data.attributes.name,
                        showtime.attributes.screen.data.attributes.cinema.data.attributes.location,
                        showtime.attributes.screen.data.attributes.cinema.data.attributes.logo.data.attributes.url
                );
                if (!isCinemaExist(cinema)) {
                    cinema.setPrice((Integer) showtime.attributes.price);
                    cinemas.add(cinema);
                }
                int id = Integer.parseInt(showtime.id);
                getCinemaFromList(cinemas, cinema)
                        .getShowtimes()
                        .add(new Showtime(
                                id, (Integer) showtime.attributes.price,
                                dateInShowTime,
                                Integer.parseInt(showtime.attributes.screen.data.id),
                                Integer.parseInt(showtime.attributes.movie.data.id)));
            }
        }
        CinemaAdapter cinemaAdapter = new CinemaAdapter(this, R.layout.cinema_item, cinemas, intent);
        cinemaListView.setAdapter(cinemaAdapter);
    }

    private Cinema getCinemaFromList(List<Cinema> cinemas, Cinema cinema) {
        for (Cinema c : cinemas) {
            if (c.getId() == cinema.getId()) {
                return c;
            }
        }
        return null;
    }

    private boolean isCinemaExist(Cinema cinema) {
        for (Cinema c : cinemas) {
            if (c.getId() == cinema.getId()) {
                return true;
            }
        }
        return false;
    }

    private void loadData() {
        sharedPreferences = getSharedPreferences("bookingMovieInfo", MODE_PRIVATE);
        tv_movieName.setText(sharedPreferences.getString("movieName", ""));

        String Token = "Bearer " +
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNzE2MTM0ODA1LCJleHAiOjE3MTg3MjY4MDV9.vQ0PURIP7d4zQUjWaSMFUQe1Ff5jpJ_OC04rzcPlH4A";
        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", Token)
                .build();
        apolloClient.query(new GetShowTimesQuery(Optional.present(sharedPreferences.getString("movieId", ""))))
                .enqueue(response -> {
                    ListShowTime = response.data.showTimes.data;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                    sdf.setTimeZone(TimeZone.getTimeZone("UTC+7"));
                    for (GetShowTimesQuery.Data1 showtime : ListShowTime) {
                        Log.d("test", showtime.attributes.show_time.toString());
                        try {
                            Date date = sdf.parse(showtime.attributes.show_time.toString());
                            if (dates.size() >= 1) {
                                Date lastDateInList = dates.get(dates.size()-1);
                                if (date.getYear() == lastDateInList.getYear()
                                        && date.getMonth() == lastDateInList.getMonth()
                                        && date.getDate() == lastDateInList.getDate())
                                    continue;
                            }
                            dates.add(date);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    runOnUiThread(() -> {
                        dateRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                        DateAdapter dateAdapter = new DateAdapter(this, dates, position -> loadCinema(dates.get(position)));
                        dateRecyclerView.setAdapter(dateAdapter);
                        loadCinema(dates.get(0));
                    });
        });


    }
}