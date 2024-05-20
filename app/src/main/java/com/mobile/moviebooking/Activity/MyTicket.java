package com.mobile.moviebooking.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.google.android.material.card.MaterialCardView;
import com.mobile.moviebooking.Adapter.TicketAdapter;
import com.mobile.moviebooking.Entity.Ticket;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class MyTicket extends AppCompatActivity {

    private TicketAdapter ticketAdapter;
    private RecyclerView recyclerView;
    private List<Ticket> listTicket = new ArrayList<Ticket>();
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

        for (int i = 0; i < 0; i++) {
            Ticket ticket = new Ticket();
            ticket.setMovieDate("2021-10-10");
            ticket.setMovieLocation("Cinestar Thủ Đức");
            ticket.setMovieName("Phim ko hay xoa app");
            ticket.setMovieTime("20:00");
            ticket.setMovieImgUrl("https://m.media-amazon.com/images/I/91+od0A3itL._AC_UF1000,1000_QL80_.jpg");
            listTicket.add(ticket);
        }

        if(!this.listTicket.isEmpty()) {
            emptyTicket.setVisibility(View.GONE);
            ticketAdapter.setData(listTicket);
        }
        else {
            emptyTicket.setVisibility(View.VISIBLE);
        }
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