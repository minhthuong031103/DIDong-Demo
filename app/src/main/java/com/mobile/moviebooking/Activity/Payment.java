package com.mobile.moviebooking.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;
import com.mobile.moviebooking.R;

public class Payment extends AppCompatActivity {
    private SharedPreferences movieInfo;
    private MaterialCardView zaloPay, momo, shopeePay, atmCard, visaCard;
    private ImageView backBtn;
    private int paymentMethod = -1;
    private void findViewById() {
        zaloPay = findViewById(R.id.Zalo);
        momo = findViewById(R.id.momo);
        shopeePay = findViewById(R.id.shopee);
        atmCard = findViewById(R.id.atm);
        visaCard = findViewById(R.id.Visa);
        backBtn = findViewById(R.id.back);
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
        movieInfo = getSharedPreferences("movieInfo", MODE_PRIVATE);

        findViewById();
        setOnclickPayment();

        backBtn.setOnClickListener(v -> {
           getOnBackPressedDispatcher().onBackPressed();
        });
    }

    private void setOnclickPayment() {
        zaloPay.setOnClickListener(v -> {
            paymentMethod = 1;
            resetSelectedPaymentMethod();
            zaloPay.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            zaloPay.setStrokeColor(getResources().getColor(R.color.duskYellow));
        });
        momo.setOnClickListener(v -> {
            paymentMethod = 2;
            resetSelectedPaymentMethod();
            momo.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            momo.setStrokeColor(getResources().getColor(R.color.duskYellow));
        });
        shopeePay.setOnClickListener(v -> {
            paymentMethod = 3;
            resetSelectedPaymentMethod();
            shopeePay.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            shopeePay.setStrokeColor(getResources().getColor(R.color.duskYellow));
        });
        atmCard.setOnClickListener(v -> {
            paymentMethod = 4;
            resetSelectedPaymentMethod();
            atmCard.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            atmCard.setStrokeColor(getResources().getColor(R.color.duskYellow));
        });
        visaCard.setOnClickListener(v -> {
            paymentMethod = 5;
            resetSelectedPaymentMethod();
            visaCard.setCardBackgroundColor(getResources().getColor(R.color.payment_selected));
            visaCard.setStrokeColor(getResources().getColor(R.color.duskYellow));
        });
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
        paymentMethod = -1;
    }
}