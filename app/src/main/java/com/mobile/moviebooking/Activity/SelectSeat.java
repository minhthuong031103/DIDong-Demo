package com.mobile.moviebooking.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.mobile.moviebooking.Adapter.SeatAdapter;
import com.mobile.moviebooking.Entity.Seat;
import com.mobile.moviebooking.R;
import com.otaliastudios.zoom.ZoomLayout;

import java.util.ArrayList;
import java.util.List;

public class SelectSeat extends AppCompatActivity {
    private GridView seatsGridView;
    private List<Seat> seats = new ArrayList<>();
    private int numRow;
    private int numCol;
    private ImageView backBtn;
    private ExtendedFloatingActionButton ctnBtn;
    private ConstraintLayout summaryLayout;
    private TextView total;
    private TextView selectedSeats;
    private int price = 50000;
    private int totalPayment = 0;
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
        setUpSeatView();

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
        numRow = 7;
        numCol = 7;
        for (char i = 'A'; i < 'H'; i++) {
        for (int j = 1; j < 8; j++) {
            if (j == 2) {
                seats.add(new Seat(1, j, i, (byte) 1));
                continue;
            }
            seats.add(new Seat(1, j, i, (byte) 0));
        }
    }
    }

    private String numberToVND(int number) {
        return String.format("%,d", number).replace(',', '.') + " VNĐ";
    }
}