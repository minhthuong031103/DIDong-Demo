package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.rocketreserver.ChangePasswordMutation;
import com.example.rocketreserver.LoginMutation;
import com.google.android.material.button.MaterialButton;
import com.mobile.moviebooking.R;

public class Profile extends AppCompatActivity {

    private ConstraintLayout logOut, ticketLayout, paymentLayout, changePasswordLayout;
    private TextView tvUsername, tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findView();
        navBar();
        logOut();
        changePassowrd();

    }

    private void changePassowrd() {
        changePasswordLayout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater builder2 = LayoutInflater.from(this);
            View dialogView = builder2.inflate(R.layout.dialog_change_password, null);

            AlertDialog alertDialog = builder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
            alertDialog.setView(dialogView);
            alertDialog.show();

            EditText edtCurrentPass = dialogView.findViewById(R.id.edtCurrPass);
            EditText edtNewPass = dialogView.findViewById(R.id.edtNewPass);
            EditText edtConfirmPass = dialogView.findViewById(R.id.edtConfirmPass);

            MaterialButton btnChangePass = dialogView.findViewById(R.id.btnChangePass);

            btnChangePass.setOnClickListener(v2 -> {

                String token = "Bearer " +  getSharedPreferences("userInfo", MODE_PRIVATE).getString("jwt", "");

                ApolloClient apolloClient = new ApolloClient.Builder()
                        .serverUrl("http://77.37.47.87:1338/graphql")
                        .addHttpHeader("Authorization", token)
                        .build();

                ChangePasswordMutation changePasswordMutation = ChangePasswordMutation.builder()
                        .currentPassword(edtCurrentPass.getText().toString())
                        .password(edtNewPass.getText().toString())
                        .passwordConfirmation(edtConfirmPass.getText().toString())
                        .build();
                apolloClient.mutation(changePasswordMutation).enqueue(apolloResponse -> {
                    if (apolloResponse.hasErrors()) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        startActivity(new Intent(Profile.this, SignIn.class));
                    }
                        });

            });

        });
    }

    private void findView() {
        logOut = findViewById(R.id.logoutLayout);
        ticketLayout = findViewById(R.id.ticketLayout);
        paymentLayout = findViewById(R.id.paymentLayout);
        changePasswordLayout = findViewById(R.id.changePasswordLayout);

        ticketLayout.setOnClickListener(v -> {
            startActivity(new Intent(Profile.this, MyTicket.class));
        });

        paymentLayout.setOnClickListener(v -> {
            startActivity(new Intent(Profile.this, TicketHistory.class));
        });


        tvUsername = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvMail);

        tvUsername.setText(getSharedPreferences("userInfo", MODE_PRIVATE).getString("userName", ""));
        tvEmail.setText(getSharedPreferences("userInfo", MODE_PRIVATE).getString("email", ""));

    }

    private void logOut() {
        logOut.setOnClickListener(v->{
            SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
            userInfo.edit().clear().apply();
            Intent intent = new Intent(Profile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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
                .setFirstSelectedPosition(3)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(Profile.this, HomePage.class));
                        break;
                    case 1:
                        startActivity(new Intent(Profile.this, MyTicket.class));
                        break;
                    case 2:
                        startActivity(new Intent(Profile.this, MovieActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(Profile.this, Profile.class));
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
}