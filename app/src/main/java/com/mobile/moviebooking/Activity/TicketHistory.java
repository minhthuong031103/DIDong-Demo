package com.mobile.moviebooking.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mobile.moviebooking.Adapter.TicketAdapter;
import com.mobile.moviebooking.Entity.Ticket;
import com.mobile.moviebooking.R;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TicketHistory extends AppCompatActivity {
    private MaterialButton btnFilter;
    private TicketAdapter ticketAdapter;
    private RecyclerView recyclerView;
    private List<Ticket> listTicket = new ArrayList<Ticket>();
    private MaterialCardView cardView;

    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ticket_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findView();
        loadTicket();
        onClickFilter();
        onPullToRefresh();
    }



    private void onPullToRefresh() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void refreshPage() {
        // empty the list
        listTicket.clear();
        loadTicket();
    }


    private void loadTicket() {
        if(!this.listTicket.isEmpty())
            this.listTicket.clear();

        for (int i = 0; i < 10; i++) {
            Ticket ticket = new Ticket();
            String date = String.format("10-%d-2023", i + 1);
            ticket.setMovieDate(date);
            ticket.setMovieLocation("Cinestar Thủ Đức");
            ticket.setMovieName("Phim ko hay xoa app");
            ticket.setMovieTime("20:00");
            ticket.setMovieImgUrl("https://m.media-amazon.com/images/I/91+od0A3itL._AC_UF1000,1000_QL80_.jpg");
            listTicket.add(ticket);
        }

        if(!this.listTicket.isEmpty()) {
            cardView.setVisibility(View.GONE);
            ticketAdapter.setData(listTicket);
        }
        else {
            cardView.setVisibility(View.VISIBLE);
        }
    }

    private void onClickFilter() {
        btnFilter.setOnClickListener(v -> {

            // write a alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LayoutInflater builder2 = LayoutInflater.from(this);
            View dialogView = builder2.inflate(R.layout.filter_dialog, null);

            AlertDialog alertDialog = builder.create();
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_bg);
            alertDialog.setView(dialogView);
            alertDialog.show();


            NumberPicker npMonth = dialogView.findViewById(R.id.npMonth);
            npMonth.setWrapSelectorWheel(false);
            npMonth.setMaxValue(12);
            npMonth.setMinValue(1);
            npMonth.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1);

            NumberPicker npYear = dialogView.findViewById(R.id.npYear);
            npYear.setWrapSelectorWheel(false);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            npYear.setMaxValue(currentYear);
            npYear.setMinValue(currentYear - 10);
            npYear.setValue(currentYear);

            MaterialButton btnApply = dialogView.findViewById(R.id.btnApply);
            btnApply.setOnClickListener(v1 -> {
                int month = npMonth.getValue();
                int year = npYear.getValue();
                loadTicket();
                ticketAdapter.filterTicket(String.valueOf(month), String.valueOf(year));
                if(ticketAdapter.getTickets().size() < 1){
                    cardView.setVisibility(View.VISIBLE);
                }
                alertDialog.dismiss();
            });

            MaterialButton btnClear = dialogView.findViewById(R.id.btnClear);
            btnClear.setOnClickListener(v1 -> {
                loadTicket();
                alertDialog.dismiss();
            });

            ImageView imvClose = dialogView.findViewById(R.id.imvClose);
            imvClose.setOnClickListener(v1 -> {
                alertDialog.dismiss();
            });


        });
    }

    private void findView() {
        btnFilter = findViewById(R.id.btnFilter);

        ticketAdapter = new TicketAdapter(this);
        ticketAdapter.setData(listTicket);

        recyclerView = findViewById(R.id.rvMyTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(ticketAdapter);

        cardView = findViewById(R.id.cardEmpty);

        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
    }
}