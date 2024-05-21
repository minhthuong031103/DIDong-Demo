package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.rocketreserver.GetTicketQuery;
import com.example.rocketreserver.MovieHomaePageQuery;
import com.google.android.material.card.MaterialCardView;
import com.mobile.moviebooking.Adapter.HomePageComingMovieAdapter;
import com.mobile.moviebooking.Adapter.HomePagePlayingMovieAdapter;
import com.mobile.moviebooking.Adapter.TicketAdapter;
import com.mobile.moviebooking.Entity.Movie;
import com.mobile.moviebooking.Entity.Ticket;
import com.mobile.moviebooking.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyTicket extends AppCompatActivity {

    private TicketAdapter ticketAdapter;
    private RecyclerView recyclerView;
    private List<Ticket> listTicket = new ArrayList<Ticket>();
    private List<Ticket> historyList = new ArrayList<Ticket>();
    private MaterialCardView emptyTicket;
    private MaterialCardView historyTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById();
        loadTicket();
        navBar();
        toHistoryTicket();

    }

    private void toHistoryTicket() {
        historyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyTicket.this, TicketHistory.class);

                Bundle args = new Bundle();
                args.putSerializable("historyTicket",(Serializable) historyList);
                intent.putExtra("BUNDLE",args);
                startActivity(intent);
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
                .setFirstSelectedPosition(1)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MyTicket.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MyTicket.this, MyTicket.class));
                        break;
                    case 2:
                        startActivity(new Intent(MyTicket.this, MovieActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MyTicket.this, Profile.class));
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

    private void loadTicket() {
        if(!this.listTicket.isEmpty())
            this.listTicket.clear();

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String token = "Bearer " + userInfo.getString("jwt", "893a13fa53a2ff80efe5b37c1fd5942434971882b53655b0542e4ccdb7ab76bbd28fbbac96939f04f01bdb1c098492f91d908e8dc38b3092f348bf2d190ffa91354f451a38afadd4063f6fcbb256e84a7b9ad7e7c8775be390ba32a68d2c393bca77d6a2031dfd3358a9760dad48ca115b7086103cd355c140aa99451fd510c0");
        String userId = userInfo.getString("userID", "6");


        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", token)
                .build();
        System.out.println(userId);

        Optional<String> id =  Optional.present(userId);

        GetTicketQuery getTicketQuery = new GetTicketQuery(id);


        apolloClient.query(getTicketQuery).enqueue(apolloResponse -> {
            //check if response is null
            if (apolloResponse.data == null) {
                System.out.println("No data found");
                return;
            }

            List<GetTicketQuery.Data1> tickets = apolloResponse.data.tickets.data;
            for (GetTicketQuery.Data1 ticket : tickets) {
                Ticket newTicket = new Ticket();

                newTicket.setId(ticket.id);

                newTicket.setMovieName(ticket.attributes.show_time.data.attributes.movie.data.attributes.title);
                newTicket.setMovieLocation(ticket.attributes.show_time.data.attributes.screen.data.attributes
                        .cinema.data.attributes.location);



                newTicket.setMovieTime(ticket.attributes.show_time.data.attributes.movie.data.attributes.duration.toString());

                newTicket.setMovieImgUrl(ticket.attributes.show_time.data.attributes
                        .movie.data.attributes.poster.data.attributes.url);

                String movieTime = ticket.attributes.show_time.data.attributes.show_time.toString();

                String formattedDateTime = formatDateTime(movieTime);
                newTicket.setMovieDate(formattedDateTime);

                String modifiedString = removeCharacterAtPosition(formattedDateTime, 6);

                if (isDateTimeSmaller(modifiedString)) {
                   // historyList.add(newTicket);
                    continue;
                }

                listTicket.add(newTicket);


                runOnUiThread(() -> {
                    if(!this.listTicket.isEmpty()) {
                        emptyTicket.setVisibility(View.GONE);
                        ticketAdapter.setData(listTicket);
                    }
                    else {
                        emptyTicket.setVisibility(View.VISIBLE);
                    }
                });
            }
        });



    }
    private String removeCharacterAtPosition(String str, int position) {
        // Check if position is valid
        if (position < 0 || position >= str.length()) {
            throw new IllegalArgumentException("Position is out of range.");
        }

        // Remove the character at the specified position
        return str.substring(0, position - 1) + str.substring(position + 1);
    }

    private boolean isDateTimeSmaller(String formattedDateTime) {
        // Parse the formatted datetime string into LocalDateTime object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm dd-MM-yyyy");
        LocalDateTime formattedDateTimeObj = LocalDateTime.parse(formattedDateTime, formatter);

        // Get the current datetime
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Compare the datetimes
        return formattedDateTimeObj.isBefore(currentDateTime);
    }

    private String formatDateTime(String dateTimeString) {
        // Split the date and time parts
        String[] parts = dateTimeString.split("T");
        String datePart = parts[0];
        String timePart = parts[1].substring(0, 5); // Extracting only HH:mm:ss part

        // Split the date into year, month, and day
        String[] dateParts = datePart.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];

        // Format the date and time string
        return timePart + " • " + day + "-" + month + "-" + year;
    }

    private void findViewById() {
        ticketAdapter = new TicketAdapter(this);
        ticketAdapter.setData(listTicket);

        emptyTicket = findViewById(R.id.emptyTicket);
        historyTicket = findViewById(R.id.cardHistory);

        recyclerView = findViewById(R.id.rvMyTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ticketAdapter);
    }
}