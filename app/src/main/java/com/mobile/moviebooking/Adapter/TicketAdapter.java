package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        holder.tvMovieTime.setText(ticket.getMovieTime());
        holder.tvMovieDate.setText(ticket.getMovieDate());
        holder.tvMovieLocation.setText(ticket.getMovieLocation());
        Glide.with(context).load(ticket.getMovieImgUrl()).into(holder.ivMovieImage);
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
        notifyDataSetChanged();
    }


    class TicketHolder extends RecyclerView.ViewHolder {

        private TextView tvMovieName;
        private TextView tvMovieTime;
        private TextView tvMovieDate;
        private TextView tvMovieLocation;
        private ImageView ivMovieImage;
        TicketHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tvMovieName);
            tvMovieTime = itemView.findViewById(R.id.tvMovieTime);
            tvMovieDate = itemView.findViewById(R.id.tvMovieDate);
            ivMovieImage = itemView.findViewById(R.id.ivMovieImage);
            tvMovieLocation = itemView.findViewById(R.id.tvMovieLocation);
        }

    }

}