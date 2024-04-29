package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mobile.moviebooking.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.DateViewHolder> {
    private Context context;
    private List<Date> dateList;
    private int selectedPosition = 0;
    public DateAdapter(Context context, List<Date> dateList) {
        this.context = context;
        this.dateList = dateList;
    }
    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item,parent,false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        Date date = dateList.get(position);
        holder.date.setText(new SimpleDateFormat("dd").format(date));
        holder.month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(date) );
        holder.dayName.setText(new SimpleDateFormat("EEE", Locale.ENGLISH).format(date));
        if (selectedPosition != position) {
            holder.fullLayout.setCardBackgroundColor(context.getResources().getColor(R.color.card_date_black));
            holder.date.setTextColor(Color.parseColor("#C2C2C2"));
            holder.month.setTextColor(Color.parseColor("#C2C2C2"));
            holder.dayName.setTextColor(Color.parseColor("#C2C2C2"));
            holder.circle.setCardBackgroundColor(context.getResources().getColor(R.color.date_grey));
        } else {
            holder.fullLayout.setCardBackgroundColor(context.getResources().getColor(R.color.yello_theme));
            holder.date.setTextColor(context.getResources().getColor(R.color.date_white));
            holder.month.setTextColor(Color.parseColor("#000000"));
            holder.dayName.setTextColor(Color.parseColor("#000000"));
            holder.circle.setCardBackgroundColor(context.getResources().getColor(R.color.date_black));
        }
        holder.fullLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList == null ? 0 : dateList.size();
    }


    public static class DateViewHolder extends RecyclerView.ViewHolder {
        private TextView date;
        private TextView month;
        private TextView dayName;
        private CardView fullLayout;
        private CardView circle;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            dayName = itemView.findViewById(R.id.dayName);
            fullLayout = itemView.findViewById(R.id.fullLayout);
            circle = itemView.findViewById(R.id.circle);
        }
    }
}
