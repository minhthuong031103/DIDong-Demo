package com.mobile.moviebooking.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.GetReservedSeatQuery;
import com.example.rocketreserver.GetSeatFromScreenQuery;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mobile.moviebooking.Adapter.SeatAdapter;
import com.mobile.moviebooking.Entity.Seat;
import com.mobile.moviebooking.R;
import com.otaliastudios.zoom.ZoomLayout;

import java.util.ArrayList;
import java.util.List;

public class SelectSeat extends AppCompatActivity {
    private GridView seatsGridView;
    private static List<Seat> seats = new ArrayList<>();
    private int numRow = 0;
    private int numCol = 0;
    private ImageView backBtn;
    private ExtendedFloatingActionButton ctnBtn;
    private ConstraintLayout summaryLayout;
    private TextView total;
    private TextView selectedSeats;
    private int price = 0;
    private int totalPayment = 0;
    private int screenId;
    private int showtimeId;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_seat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById();

        loadSeat();

        backBtn.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        ctnBtn.setClickable(false);

        Intent intent = new Intent(this, SelectFood.class);
        ctnBtn.setOnClickListener(v -> {
            intent.putExtra("totalPayment", totalPayment);
            startActivity(intent);
        });
    }

    private void findViewById() {
        ctnBtn = findViewById(R.id.btn);
        backBtn = findViewById(R.id.back);
        seatsGridView = findViewById(R.id.gv_seat);
        summaryLayout = findViewById(R.id.summary);
        total = findViewById(R.id.total);
        selectedSeats = findViewById(R.id.selectedSeats);
    }

    private void setUpSeatView() {
        seatsGridView.setNumColumns(numCol);
        SeatAdapter seatAdapter = new SeatAdapter(this, R.layout.seat_item, seats);
        seatsGridView.setAdapter(seatAdapter);


        DisplayMetrics displayMetrics = new  DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        ViewGroup.LayoutParams layoutParams  = seatsGridView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = convertDpToPixels(numRow * 36 + 8, this) ;
        seatsGridView.setLayoutParams(layoutParams);

        ZoomLayout zoomLayout = findViewById(R.id.zoomLayout);
        zoomLayout.getLayoutParams().height = layoutParams.height;

        seatsGridView.setOnItemClickListener((parent, view, position, id) -> {
            Seat seat = seats.get(position);
            if (seat.getStatus() == 0) {
                seat.setStatus((byte) 2);
                seats.set(position, seat);
            } else if (seats.get(position).getStatus() == 2) {
                seat.setStatus((byte) 0);
                seats.set(position, seat);
            } else {
                return;
            }
            seatAdapter.notifyDataSetChanged();

            updateSummary();
        });
    }

    private void updateSummary() {
        summaryLayout.setVisibility(View.VISIBLE);
        ctnBtn.setVisibility(View.VISIBLE);

        int totalSeat = 0;
        StringBuilder selectedSeat = new StringBuilder();
        for (Seat seat : seats) {
            if (seat.getStatus() == 2) {
                totalSeat++;
                selectedSeat.append(seat.getRow()).append(seat.getSeatNumber()).append(", ");
            }
        }
        totalPayment = totalSeat * price;
        if (totalSeat > 0) {
            selectedSeat = new StringBuilder(selectedSeat.substring(0, selectedSeat.length() - 2));
            ctnBtn.setClickable(true);
            ctnBtn.setBackgroundColor(getResources().getColor(R.color.bg_enabled_btn));
            ctnBtn.setTextColor(getResources().getColor(R.color.txt_enabled_btn));
        } else {
            ctnBtn.setBackgroundColor(getResources().getColor(R.color.bg_disabled_btn));
            ctnBtn.setTextColor(getResources().getColor(R.color.txt_disabled_btn));
            ctnBtn.setClickable(false);
        }

        selectedSeats.setText( selectedSeat + " (" + totalSeat + ")");

        total.setText(numberToVND(totalPayment));
    }

    public int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

    private void loadSeat() {
        price = getIntent().getIntExtra("price", 0);
        screenId = getIntent().getIntExtra("screenId", -1);
        showtimeId = getIntent().getIntExtra("showtimeId", -1);
        String Token = "Bearer " +
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MywiaWF0IjoxNzE2MTM0ODA1LCJleHAiOjE3MTg3MjY4MDV9.vQ0PURIP7d4zQUjWaSMFUQe1Ff5jpJ_OC04rzcPlH4A";
        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", Token)
                .build();
        apolloClient.query(new GetSeatFromScreenQuery(Optional.present(String.valueOf(screenId))))
                .enqueue(response -> {
                    List<GetSeatFromScreenQuery.Data2> ApolloSeats = response.data.screen.data.attributes.seats.data;
                    for (GetSeatFromScreenQuery.Data2 seat : ApolloSeats) {
                        seats.add(new Seat(
                                Integer.parseInt(seat.id),
                                seat.attributes.seat_number.intValue(),
                                seat.attributes.seat_row.charAt(0),
                                (byte)0
                        ));
                        numRow = Math.max(numRow, seat.attributes.seat_row.charAt(0) - 'A' + 1);
                        numCol = Math.max(numCol, seat.attributes.seat_number.intValue());
                    }
                    apolloClient.query(new GetReservedSeatQuery(Optional.present(String.valueOf(showtimeId))))
                            .enqueue(response2 -> {
                                List<GetReservedSeatQuery.Data1> Tickets = response2.data.tickets.data;
                                for (GetReservedSeatQuery.Data1 ticket : Tickets) {
                                    List<GetReservedSeatQuery.Data2> SeatInTicket = ticket.attributes.seats.data;
                                    for (GetReservedSeatQuery.Data2 seat : SeatInTicket) {
                                        for (Seat s : seats) {
                                            if (s.getId() == Integer.parseInt(seat.id)) {
                                                s.setStatus((byte) 1);
                                            }
                                        }
                                    }
                                }
                                runOnUiThread(() -> {
                                    setUpSeatView();
                                });
                            });
                });
    }

    private String numberToVND(int number) {
        return String.format("%,d", number).replace(',', '.') + " VND";
    }
    public static List<Seat> selectedSeats(){
        List<Seat> selectedSeats = new ArrayList<>();
        for (Seat seat : seats) {
            if (seat.getStatus() == 2) {
                selectedSeats.add(seat);
            }
        }
        return selectedSeats;
    }
}