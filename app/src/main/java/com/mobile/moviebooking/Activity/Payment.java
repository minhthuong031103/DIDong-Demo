package com.mobile.moviebooking.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.bumptech.glide.Glide;
import com.example.rocketreserver.BookFoodMutation;
import com.example.rocketreserver.BookTicketMutation;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobile.moviebooking.Entity.Food;
import com.mobile.moviebooking.Entity.Seat;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class Payment extends AppCompatActivity {
    private SharedPreferences movieInfo;
    private MaterialCardView zaloPay, momo, shopeePay, atmCard, visaCard;
    private ImageView backBtn;
    private int paymentMethod;
    ExtendedFloatingActionButton paymentBtn;
    private ImageView poster;
    private TextView tv_movieName;
    private TextView tv_genre;
    private TextView tv_cinema;
    private TextView tv_time;
    private TextView tv_seat;
    private TextView tv_food;
    private TextView tv_total;
    private int totalPayment;
    private void findViewById() {
        poster = findViewById(R.id.img_movie);
        tv_movieName = findViewById(R.id.tv_movie_name);
        tv_genre = findViewById(R.id.tv_movie_genre);
        tv_cinema = findViewById(R.id.tv_movie_location);
        tv_time = findViewById(R.id.textView9);
        tv_seat = findViewById(R.id.tv_seat);
        tv_food = findViewById(R.id.tv_food);
        tv_total = findViewById(R.id.tv_total);

        zaloPay = findViewById(R.id.Zalo);
        momo = findViewById(R.id.momo);
        shopeePay = findViewById(R.id.shopee);
        atmCard = findViewById(R.id.atm);
        visaCard = findViewById(R.id.Visa);
        backBtn = findViewById(R.id.back);
        paymentBtn = findViewById(R.id.btn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_payment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        movieInfo = getSharedPreferences("bookingMovieInfo", MODE_PRIVATE);
        paymentMethod = -1;

        findViewById();
        loadData();
        setOnclickPayment();

        backBtn.setOnClickListener(v -> {
           getOnBackPressedDispatcher().onBackPressed();
        });

        paymentBtn.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
            String userID = sharedPreferences.getString("userID", "4");
            String jwt = sharedPreferences.getString("jwt", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NCwiaWF0IjoxNzE2MjA0MjYxLCJleHAiOjE3MTg3OTYyNjF9.4zeG1-D95DoogNfFumqKJhgpXib2nKK8o7-mfBpG4Bo");

            String Token = "Bearer " + jwt;
            ApolloClient apolloClient = new ApolloClient.Builder()
                    .serverUrl("http://77.37.47.87:1338/graphql")
                    .addHttpHeader("Authorization", Token)
                    .build();
            Optional<String> showtimeID = Optional.present(movieInfo.getString("showtimeId", ""));
            List<String> seatsString = new ArrayList<>();
            List<Seat> selectedSeats = SelectSeat.selectedSeats();
            for (Seat s: selectedSeats) {
                seatsString.add(String.valueOf(s.getId()));
            }
            Optional<List<String>> seats = Optional.present(seatsString);
            apolloClient.mutation(
                    new BookTicketMutation(
                            showtimeID,
                            seats,
                            Optional.present(totalPayment),
                            Optional.present(userID)))
                    .enqueue(response -> {
                        List<Food> foods = SelectFood.getSelectedFoods();
                        for (Food food: foods) {
                            apolloClient.mutation(
                                    new BookFoodMutation(
                                            Optional.present(response.data.createTicket.data.id),
                                            Optional.present(String.valueOf(food.getId())),
                                            Optional.present(food.getQuantity())
                                    )).enqueue(response1 -> {
                                Log.d("test", "onCreate: " + response1.data);
                            });
                        }
                        Intent intent = new Intent(Payment.this, MyTicket.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    });
        });
    }

    private void loadData() {
        Glide.with(this).load(movieInfo.getString("moviePoster", "")).into(poster);
        tv_movieName.setText(movieInfo.getString("movieName", ""));
        tv_genre.setText(movieInfo.getString("genre", ""));
        tv_cinema.setText(movieInfo.getString("cinemaName", ""));
        tv_time.setText(movieInfo.getString("showtime", ""));
        Log.d("test", "loadData: " + SelectSeat.selectedSeats().get(0).getRow());
        List<Seat> seats = SelectSeat.selectedSeats();
        String seat = "";
        for (Seat s : seats) {
            seat += String.valueOf(s.getRow()) + String.valueOf(s.seatNumber()) + ", ";
        }
        tv_seat.setText(seat.substring(0, seat.length() - 2));

        List<Food> foods = SelectFood.getSelectedFoods();
        String food = "";
        for (Food f : foods) {
            food += f.getName() + "\n";
        }
        tv_food.setText(food);

        totalPayment = getIntent().getIntExtra("totalPayment", 0);
        tv_total.setText(String.format("%,d", totalPayment).replace(',', '.') + " VND");
    }

    private void setOnclickPayment() {
        zaloPay.setOnClickListener(v -> {
            paymentMethod = 1;
            resetSelectedPaymentMethod();
            zaloPay.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            zaloPay.setStrokeColor(getResources().getColor(R.color.duskYellow));
            setEnableButton();
        });
        momo.setOnClickListener(v -> {
            paymentMethod = 2;
            resetSelectedPaymentMethod();
            momo.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            momo.setStrokeColor(getResources().getColor(R.color.duskYellow));
            setEnableButton();
        });
        shopeePay.setOnClickListener(v -> {
            paymentMethod = 3;
            resetSelectedPaymentMethod();
            shopeePay.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            shopeePay.setStrokeColor(getResources().getColor(R.color.duskYellow));
            setEnableButton();
        });
        atmCard.setOnClickListener(v -> {
            paymentMethod = 4;
            resetSelectedPaymentMethod();
            atmCard.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            atmCard.setStrokeColor(getResources().getColor(R.color.duskYellow));
            setEnableButton();
        });
        visaCard.setOnClickListener(v -> {
            paymentMethod = 5;
            resetSelectedPaymentMethod();
            visaCard.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            visaCard.setStrokeColor(getResources().getColor(R.color.duskYellow));
            setEnableButton();
        });
    }


    private void setEnableButton() {
        if (paymentMethod != -1) {
            paymentBtn.setBackgroundColor(getResources().getColor(R.color.bg_enabled_btn));
            paymentBtn.setTextColor(getResources().getColor(R.color.txt_enabled_btn));
            paymentBtn.setClickable(true);
            paymentBtn.setText("payment");
        }
    }

    private void resetSelectedPaymentMethod(){
        zaloPay.setCardBackgroundColor(getResources().getColor(R.color.bg_seat_available));
        zaloPay.setStrokeColor(getResources().getColor(R.color.bg_seat_available));
        momo.setCardBackgroundColor(getResources().getColor(R.color.bg_seat_available));
        momo.setStrokeColor(getResources().getColor(R.color.bg_seat_available));
        shopeePay.setCardBackgroundColor(getResources().getColor(R.color.bg_seat_available));
        shopeePay.setStrokeColor(getResources().getColor(R.color.bg_seat_available));
        atmCard.setCardBackgroundColor(getResources().getColor(R.color.bg_seat_available));
        atmCard.setStrokeColor(getResources().getColor(R.color.bg_seat_available));
        visaCard.setCardBackgroundColor(getResources().getColor(R.color.bg_seat_available));
        visaCard.setStrokeColor(getResources().getColor(R.color.bg_seat_available));
    }
}