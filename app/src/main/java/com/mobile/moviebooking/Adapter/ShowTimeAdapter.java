package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.mobile.moviebooking.Entity.Showtime;
import com.mobile.moviebooking.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ShowTimeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Showtime> list;
    private int selectedPosition = -1;

    public ShowTimeAdapter(Context context, int layout, List<Showtime> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    static class ViewHolder {
        CardView cardView;
        TextView time;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, layout, null);
            holder = new ViewHolder();
            holder.cardView = convertView.findViewById(R.id.card);
            holder.time = convertView.findViewById(R.id.showtime);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Showtime showtime = list.get(position);
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(showtime.getShowtime());
        holder.time.setText(time);

        if (selectedPosition == position) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.yello_theme));
            holder.time.setTextColor(ContextCompat.getColor(context, R.color.date_black));
        } else {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.date_grey));
            holder.time.setTextColor(ContextCompat.getColor(context, R.color.date_white));
        }

        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
