package com.mobile.moviebooking.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.bumptech.glide.Glide;
import com.example.rocketreserver.GetTicketDetailQuery;
import com.example.rocketreserver.GetTicketQuery;
import com.example.rocketreserver.MovieHomaePageQuery;
import com.mobile.moviebooking.Entity.Ticket;
import com.mobile.moviebooking.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TicketDetail extends AppCompatActivity {
    private ImageView ivMovieImage;
    private TextView tvMovieName, tvMovieDuration, tvMovieGenre,
            tvMovieDate, tvMovieSeats, tvPrice, tvMovieLocation, tvMovieAddress, tvMovieScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findView();

        String id = getIntent().getStringExtra("ticketId");
        loadData(id);
    }



    private void loadData(String id) {

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String token = "Bearer " + userInfo.getString("jwt", "893a13fa53a2ff80efe5b37c1fd5942434971882b53655b0542e4ccdb7ab76bbd28fbbac96939f04f01bdb1c098492f91d908e8dc38b3092f348bf2d190ffa91354f451a38afadd4063f6fcbb256e84a7b9ad7e7c8775be390ba32a68d2c393bca77d6a2031dfd3358a9760dad48ca115b7086103cd355c140aa99451fd510c0");

        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", token)
                .build();

        Optional<String> ticketId = Optional.present(id);
        GetTicketDetailQuery getTicketDetailQuery = new GetTicketDetailQuery(ticketId);


        apolloClient.query(getTicketDetailQuery).enqueue(apolloResponse -> {
            //check if response is null
            if (apolloResponse.data == null) {
                System.out.println("No data found");
                return;
            }

            GetTicketDetailQuery.Data1 ticket = apolloResponse.data.ticket.data;
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

            newTicket.setMovieAddress(ticket.attributes.show_time.data.attributes.screen.data.attributes
                    .cinema.data.attributes.location);

            String movieGenre = "";
            List<GetTicketDetailQuery.Data7> movie_genres = ticket.attributes.show_time.data.attributes.movie.data.attributes.movie_genres.data;
            for (GetTicketDetailQuery.Data7 genre: movie_genres) {
                movieGenre += genre.attributes.name + ", ";
            }
            newTicket.setMovieGenres(movieGenre.substring(0, movieGenre.length()-2));

            String ticketSeats = "";
            List<GetTicketDetailQuery.Data8> ticket_seats = ticket.attributes.seats.data;
            for(GetTicketDetailQuery.Data8 seat: ticket_seats) {
                System.out.println(seat.attributes.seat_row + seat.attributes.seat_number);
                ticketSeats += String.valueOf(seat.attributes.seat_row)+ String.valueOf(seat.attributes.seat_number) + ", ";
            }
            newTicket.setMovieSeat(ticketSeats.substring(0, ticketSeats.length()-2));

            //number to vnd price
            int number   = Integer.parseInt(ticket.attributes.total.toString());
            String price = String.format("%,d", number).replace(',', '.' ) + " VND";

            newTicket.setTicketPrice(price);

            newTicket.setTicketScreen(ticket.attributes.show_time.data.attributes.screen.data.attributes.screen_number.toString());

                runOnUiThread(() -> {
                    tvMovieAddress.setText(newTicket.getMovieAddress());
                    tvMovieName.setText(newTicket.getMovieName());
                    tvMovieDuration.setText(newTicket.getMovieTime() + " min");
                    tvMovieDate.setText(newTicket.getMovieDate());
                    tvMovieGenre.setText(newTicket.getMovieGenres());
                    tvMovieLocation.setText(newTicket.getMovieLocation());
                    tvMovieAddress.setText(newTicket.getMovieAddress());
                    tvMovieSeats.setText(newTicket.getMovieSeat());
                    tvPrice.setText(newTicket.getTicketPrice());
                    tvMovieScreen.setText("Screen " + newTicket.getTicketScreen());
                    Glide.with(this).load(newTicket.getMovieImgUrl()).into(ivMovieImage);
                });

            });
        };


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


    private void findView() {
        tvMovieAddress = findViewById(R.id.tvMovieAddress);
        tvMovieLocation = findViewById(R.id.tvMovieLocation);
        tvMovieSeats = findViewById(R.id.tvMovieSeats);
        tvMovieDate = findViewById(R.id.tvMovieDate);
        tvMovieGenre = findViewById(R.id.tvMovieGenre);
        tvMovieDuration = findViewById(R.id.tvMovieDuration);
        tvMovieName = findViewById(R.id.tvMovieName);
        ivMovieImage = findViewById(R.id.ivMovieImage);
        tvPrice = findViewById(R.id.tvPrice);
        ivMovieImage = findViewById(R.id.ivMovieImageDetail);
        tvMovieScreen = findViewById(R.id.tvMovieScreen);
    }
}