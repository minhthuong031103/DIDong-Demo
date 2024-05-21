package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.mobile.moviebooking.R;

public class Profile extends AppCompatActivity {

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

        navBar();


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