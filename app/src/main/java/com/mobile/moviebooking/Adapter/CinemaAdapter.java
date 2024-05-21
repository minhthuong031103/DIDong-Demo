package com.mobile.moviebooking.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.mobile.moviebooking.Entity.Cinema;
import com.mobile.moviebooking.Entity.Showtime;
import com.mobile.moviebooking.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CinemaAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Cinema> list;
    private List<ShowTimeAdapter> showTimeAdapters = new ArrayList<>();
    private Intent intent;

    public CinemaAdapter(Context context, int layout, List<Cinema> list, Intent intent) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.intent = intent;
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
        return list.get(position).getId();
    }

    private class ViewHolder {
        TextView name;
        TextView address;
        ImageView logo;
        ImageView arrow;
        CardView expand;
        TextView price;
        GridView showtime;
        ConstraintLayout section;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Cinema cinema = list.get(position);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(context, layout, null);
            viewHolder.name = view.findViewById(R.id.name);
            viewHolder.address = view.findViewById(R.id.address);
            viewHolder.logo = view.findViewById(R.id.logo);
            viewHolder.arrow = view.findViewById(R.id.arrow);
            viewHolder.expand = view.findViewById(R.id.expand);
            viewHolder.price = view.findViewById(R.id.price);
            viewHolder.showtime = view.findViewById(R.id.showtimes);
            viewHolder.section = view.findViewById(R.id.section);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(cinema.getName());
        viewHolder.address.setText(cinema.getAddress());
        viewHolder.price.setText(NumberToPrice(cinema.getPrice()) + " VND");
        Glide.with(context).load(cinema.getLogoUrl()).into(viewHolder.logo);

        viewHolder.section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cinema.isExpanded) {
                    viewHolder.expand.setVisibility(View.GONE);
                    viewHolder.arrow.setImageResource(R.drawable.bh_down);
                    cinema.isExpanded = false;
                } else {
                    viewHolder.expand.setVisibility(View.VISIBLE);
                    viewHolder.arrow.setImageResource(R.drawable.bh_up);
                    cinema.isExpanded = true;
                    displayShowtimes(viewHolder.showtime, cinema.getShowtimes());
                }
            }
            private void displayShowtimes(GridView showtime, List<Showtime> showtimes) {
                ViewGroup.LayoutParams layoutParams  = showtime.getLayoutParams();
                layoutParams.height = convertDpToPixels(12 + 58 * (int) (showtimes.size() / 4 + (showtimes.size() % 4 == 0 ? 0 : 1)), context);
                ShowTimeAdapter showTimeAdapter = (ShowTimeAdapter) showtime.getAdapter();
                if(showTimeAdapter == null){
                    showTimeAdapter = new ShowTimeAdapter(context, R.layout.showtime_item, showtimes);
                    ShowTimeAdapter finalShowTimeAdapter = showTimeAdapter;
                    showTimeAdapters.add(showTimeAdapter);
                    showtime.setAdapter(showTimeAdapter);
                    showtime.setOnItemClickListener((parent1, view1, position1, id) -> {
                        finalShowTimeAdapter.setSelectedPosition(position1);
                        finalShowTimeAdapter.notifyDataSetChanged();
                        new Thread(() -> {
                            android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());
                            handler.post(() -> {
                                for (ShowTimeAdapter adapter : showTimeAdapters) {
                                    if (adapter != finalShowTimeAdapter && adapter.getSelectedPosition() != -1) {
                                        adapter.setSelectedPosition(-1);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                        }).start();
                        intent.putExtra("showtimeId", showtimes.get(position1).getId());
                        intent.putExtra("screenId", showtimes.get(position1).getScreenId());
                        intent.putExtra("price", showtimes.get(position1).getPrice());
                        SharedPreferences.Editor editor =
                                context.getSharedPreferences("bookingMovieInfo", Context.MODE_PRIVATE).edit();
                        editor.putString("cinemaName", viewHolder.name.getText().toString());
                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy ' • ' HH:mm");
                        editor.putString("showtime", sdf.format(showtimes.get(position1).getShowtime()));
                        editor.putString("showtimeId", String.valueOf(showtimes.get(position1).getId()));
                        editor.apply();
                    });
                }
            }
        });

        return view;
    }
    public int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
    public String NumberToPrice(int price){
        return String.format("%,d", price).replace(',', '.');
    }
}