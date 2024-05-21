package com.mobile.moviebooking.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.runtime.java.ApolloClient;
import com.example.rocketreserver.GetTicketQuery;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.mobile.moviebooking.Adapter.TicketAdapter;
import com.mobile.moviebooking.Entity.Ticket;
import com.mobile.moviebooking.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TicketHistory extends AppCompatActivity {
    private MaterialButton btnFilter;
    private TicketAdapter ticketAdapter;
    private RecyclerView recyclerView;
    private List<Ticket> listTicket = new ArrayList<Ticket>();
    private MaterialCardView cardView;
    private ImageView ivBackToMyTicket;
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

        ivBackToMyTicket.setOnClickListener(v -> {
            finish();
        });
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
        if (!this.listTicket.isEmpty())
            this.listTicket.clear();

        SharedPreferences userInfo = getSharedPreferences("userInfo", MODE_PRIVATE);
        String token = "Bearer " + userInfo.getString("jwt", "893a13fa53a2ff80efe5b37c1fd5942434971882b53655b0542e4ccdb7ab76bbd28fbbac96939f04f01bdb1c098492f91d908e8dc38b3092f348bf2d190ffa91354f451a38afadd4063f6fcbb256e84a7b9ad7e7c8775be390ba32a68d2c393bca77d6a2031dfd3358a9760dad48ca115b7086103cd355c140aa99451fd510c0");
        String userId = userInfo.getString("userID", "1");


        ApolloClient apolloClient = new ApolloClient.Builder()
                .serverUrl("http://77.37.47.87:1338/graphql")
                .addHttpHeader("Authorization", token)
                .build();

        Optional<String> id = Optional.present("6");

        GetTicketQuery getTicketQuery = new GetTicketQuery(id);


        apolloClient.query(getTicketQuery).enqueue(apolloResponse -> {
            //check if response is null
            if (apolloResponse.data == null) {
                System.out.println("No data found");
                return;
            }

            List<GetTicketQuery.Data1> tickets = apolloResponse.data.tickets.data;
            for (GetTicketQuery.Data1 ticket : tickets) {
                Ticket newTicket = new Ticket();
                newTicket.setMovieName(ticket.attributes.show_time.data.attributes.movie.data.attributes.title);
                newTicket.setMovieLocation(ticket.attributes.show_time.data.attributes.screen.data.attributes
                        .cinema.data.attributes.location);


                newTicket.setMovieTime(ticket.attributes.show_time.data.attributes.movie.data.attributes.duration.toString());

                newTicket.setMovieImgUrl(ticket.attributes.show_time.data.attributes
                        .movie.data.attributes.poster.data.attributes.url);

                String movieTime = ticket.attributes.show_time.data.attributes.show_time.toString();

                String formattedDateTime = formatDateTime(movieTime);
                newTicket.setMovieDate(formattedDateTime);

                String modifiedString = removeCharacterAtPosition(formattedDateTime, 6);

                if (isDateTimeSmaller(modifiedString)) {
                    listTicket.add(newTicket);
                }
            }

            runOnUiThread(() -> {
                if(!this.listTicket.isEmpty()) {
                    cardView.setVisibility(View.GONE);
                    ticketAdapter.setData(listTicket);
                }
                else {
                    cardView.setVisibility(View.VISIBLE);
                }
            });
        });
    }
    private String removeCharacterAtPosition(String str, int position) {
        // Check if position is valid
        if (position < 0 || position >= str.length()) {
            throw new IllegalArgumentException("Position is out of range.");
        }

        // Remove the character at the specified position
        return str.substring(0, position - 1) + str.substring(position + 1);
    }

    private boolean isDateTimeSmaller(String formattedDateTime) {
        // Parse the formatted datetime string into LocalDateTime object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm dd-MM-yyyy");
        LocalDateTime formattedDateTimeObj = LocalDateTime.parse(formattedDateTime, formatter);

        // Get the current datetime
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Compare the datetimes
        return formattedDateTimeObj.isBefore(currentDateTime);
    }

    private String formatDateTime(String dateTimeString) {
        // Split the date and time parts
        String[] parts = dateTimeString.split("T");
        String datePart = parts[0];
        String timePart = parts[1].substring(0, 5); // Extracting only HH:mm:ss part

        // Split the date into year, month, and day
        String[] dateParts = datePart.split("-");
        String year = dateParts[0];
        String month = dateParts[1];
        String day = dateParts[2];

        // Format the date and time string
        return timePart + " • " + day + "-" + month + "-" + year;
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
            String[] displayValues = new String[12];
            for (int i = 0; i < 12; i++) {
                displayValues[i] = String.format("%02d", i + 1);
            }
            npMonth.setDisplayedValues(displayValues);

            npMonth.setValue(Calendar.getInstance().get(Calendar.MONTH) + 1);

            NumberPicker npYear = dialogView.findViewById(R.id.npYear);
            npYear.setWrapSelectorWheel(false);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            npYear.setMaxValue(currentYear);
            npYear.setMinValue(currentYear - 10);
            npYear.setValue(currentYear);

            MaterialButton btnApply = dialogView.findViewById(R.id.btnApply);
            btnApply.setOnClickListener(v1 -> {
                System.out.println(npMonth.getValue());
                String month = displayValues[npMonth.getValue() - 1];
                int year = npYear.getValue();
                //loadTicket();
                filterTicket(String.valueOf(month), String.valueOf(year));
                if(ticketAdapter.getTickets().isEmpty()){
                    System.out.println("Empty");
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

    private void filterTicket(String month, String year) {
        List<Ticket> filtered = new ArrayList<>();
        for (int i = 0; i < listTicket.size(); i++) {

            String dateStr = listTicket.get(i).getMovieDate().substring(8);
            String[] date = dateStr.split("-");

            System.out.println(date[1] + " " + date[2] + " " + month + " " + year);

            if (date[1].equals(month) && date[2].equals(year)) {
                filtered.add(listTicket.get(i));
            }
        }
        if(!this.listTicket.isEmpty()) {
            cardView.setVisibility(View.GONE);
            ticketAdapter.setData(filtered);
        }
        else {
            cardView.setVisibility(View.VISIBLE);
        }

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

        ivBackToMyTicket = findViewById(R.id.ivBackToMyTicket);

    }
}