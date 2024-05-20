package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.material.tabs.TabLayout;
import com.mobile.moviebooking.Adapter.MovieAdapter;
import com.mobile.moviebooking.Entity.Movie;
import com.mobile.moviebooking.Fragment.NowPlayingFragment;
import com.mobile.moviebooking.Fragment.UpComingFragment;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class MovieActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;

    private FrameLayout frameLayout;

    private TabLayout tabLayout;
    List<Movie> listMovie = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findView();

        frameConfig();

        navBar();
    }

    private void frameConfig() {
        getSupportFragmentManager().beginTransaction().replace(R.id.flMovie, new NowPlayingFragment()).commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new NowPlayingFragment();
                        break;
                    case 1:
                        selectedFragment = new UpComingFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.flMovie, selectedFragment).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void findView() {
        frameLayout = (FrameLayout) findViewById(R.id.flMovie);
        tabLayout = (TabLayout) findViewById(R.id.tlMovie);
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
                .setFirstSelectedPosition(2)
                .initialise();

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MovieActivity.this, MainActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MovieActivity.this, MyTicket.class));
                        break;
                    case 2:
                        startActivity(new Intent(MovieActivity.this, MovieActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MovieActivity.this, Profile.class));
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