package com.mobile.moviebooking.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.mobile.moviebooking.Activity.TicketDetail;
import com.mobile.moviebooking.Entity.Ticket;
import com.mobile.moviebooking.R;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketHolder> {

    private List<Ticket> tickets = new ArrayList<>();
    private Context context;

    public TicketAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_ticket, parent, false);
        return new TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        Ticket ticket = tickets.get(position);
        holder.tvMovieName.setText(ticket.getMovieName());
        holder.tvMovieDuration.setText(ticket.getMovieTime());
        holder.tvMovieDate.setText(ticket.getMovieDate());
        holder.tvMovieLocation.setText(ticket.getMovieLocation());
        Glide.with(context).load(ticket.getMovieImgUrl()).into(holder.ivMovieImage);


        holder.cardTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TicketDetail.class);
                intent.putExtra("ticketId", ticket.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (tickets != null) {
            return tickets.size();
        }
        return 0;
    }

    public void setData(List<Ticket> list) {
        this.tickets = list;
//        if(tickets.size() < 1){
//
//        }
        notifyDataSetChanged();
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void filterTicket(String month, String year) {
        List<Ticket> filtered = new ArrayList<>();
        for (int i = 0; i < tickets.size(); i++) {

            String dateStr = tickets.get(i).getMovieDate().substring(8);
            String[] date = dateStr.split("-");

            if (date[1].equals(month) && date[2].equals(year)) {
                filtered.add(tickets.get(i));
            }
        }
        setData(filtered);
    }


    class TicketHolder extends RecyclerView.ViewHolder {

        private TextView tvMovieName;
        private TextView tvMovieDuration;
        private TextView tvMovieDate;
        private TextView tvMovieLocation;
        private ImageView ivMovieImage;

        private MaterialCardView cardTicket;
        TicketHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tvMovieName);
            tvMovieDuration = itemView.findViewById(R.id.tvMovieTime);
            tvMovieDate = itemView.findViewById(R.id.tvMovieDate);
            ivMovieImage = itemView.findViewById(R.id.ivMovieImage);
            tvMovieLocation = itemView.findViewById(R.id.tvMovieLocation);
            cardTicket = itemView.findViewById(R.id.cardTicket);
        }

    }

}