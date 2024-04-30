package com.mobile.moviebooking.Adapter;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.mobile.moviebooking.Entity.Seat;
import com.mobile.moviebooking.R;

import java.util.ConcurrentModificationException;
import java.util.List;

public class SeatAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Seat> seats;

    public SeatAdapter(Context context, int layout, List<Seat> seats) {
        this.context = context;
        this.layout = layout;
        this.seats = seats;
    }
    @Override
    public int getCount() {
        return seats.size();
    }

    @Override
    public Object getItem(int position) {
        return seats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return seats.get(position).getId();
    }

    class ViewHolder {
        private TextView seat;
        private CardView fullLayout;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Seat seat = seats.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, layout, null);
            holder.seat = convertView.findViewById(com.mobile.moviebooking.R.id.txt_seat);
            holder.fullLayout = convertView.findViewById(com.mobile.moviebooking.R.id.seat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.seat.setText(seat.getSeatRow() + String.valueOf(seat.getSeatNumber()));
        if (seat.getStatus() == (byte) 0){
            holder.seat.setTextColor(context.getResources().getColor(R.color.txt_seat_available));
            holder.fullLayout.setCardBackgroundColor(context.getResources().getColor(R.color.bg_seat_available));
        } else if (seat.getStatus() == (byte) 1) {
            holder.seat.setTextColor(context.getResources().getColor(R.color.txt_seat_reserved));
            holder.fullLayout.setCardBackgroundColor(context.getResources().getColor(R.color.bg_seat_reserved));
        } else {
            holder.seat.setTextColor(context.getResources().getColor(R.color.txt_seat_selected));
            holder.fullLayout.setCardBackgroundColor(context.getResources().getColor(R.color.bg_seat_selected));
        }

        return convertView;
    }
}
