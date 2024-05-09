package com.mobile.moviebooking.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.moviebooking.Adapter.TicketAdapter;
import com.mobile.moviebooking.Entity.Ticket;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class MyTicket extends AppCompatActivity {

    private TicketAdapter ticketAdapter;
    private RecyclerView recyclerView;
    private List<Ticket> listTicket = new ArrayList<Ticket>();


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

        loadTicket();
        findViewById();





    }

    private void loadTicket() {
        for (int i = 0; i < 10; i++) {
            Ticket ticket = new Ticket();
            ticket.setMovieDate("2021-10-10");
            ticket.setMovieLocation("Cinestar Thủ Đức");
            ticket.setMovieName("Phim ko hay xoa app");
            ticket.setMovieTime("20:00");
            ticket.setMovieImgUrl("https://m.media-amazon.com/images/I/91+od0A3itL._AC_UF1000,1000_QL80_.jpg");
            listTicket.add(ticket);
        }
    }

    private void findViewById() {
        ticketAdapter = new TicketAdapter(this);
        ticketAdapter.setData(listTicket);

        recyclerView = findViewById(R.id.rvMyTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ticketAdapter);
    }
}