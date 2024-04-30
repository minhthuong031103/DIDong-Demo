package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.moviebooking.Activity.SelectFood;
import com.mobile.moviebooking.Entity.Food;
import com.mobile.moviebooking.R;

import java.util.List;

public class FoodAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Food> foods;

    public FoodAdapter(Context context, int layout, List<Food> foods) {
        this.context = context;
        this.layout = layout;
        this.foods = foods;
    }
    @Override
    public int getCount() {
        return foods.size();
    }

    @Override
    public Object getItem(int position) {
        return foods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return foods.get(position).getId();
    }

    class ViewHolder {
        ImageView imgFood, imgAdd, imgRemove;
        TextView tvName, tvDescription, tvPrice, tvQuantity;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Food food = foods.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, layout, null);
            holder.imgFood = convertView.findViewById(R.id.cuscart_imgfood);
            holder.imgAdd = convertView.findViewById(R.id.imageView3);
            holder.imgRemove = convertView.findViewById(R.id.imageView2);
            holder.tvName = convertView.findViewById(R.id.name);
            holder.tvDescription = convertView.findViewById(R.id.description);
            holder.tvPrice = convertView.findViewById(R.id.price);
            holder.tvQuantity = convertView.findViewById(R.id.quantity);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(food.getName());
        holder.tvDescription.setText(food.getDescription());
        holder.tvPrice.setText(numberToVND(food.getPrice()));
        holder.tvQuantity.setText(String.valueOf(food.getQuantity()));
        Glide.with(context).load(food.getImage()).into(holder.imgFood);

        holder.imgAdd.setOnClickListener(v -> {
            food.setQuantity(food.getQuantity() + 1);
            notifyDataSetChanged();
            ((SelectFood)context).updateTotalPayment();
        });
        holder.imgRemove.setOnClickListener(v -> {
            if (food.getQuantity() > 0) {
                food.setQuantity(food.getQuantity() - 1);
                notifyDataSetChanged();
                ((SelectFood)context).updateTotalPayment();
            }
        });
        return convertView;
    }
    private String numberToVND(int number) {
        return String.format("%,d", number).replace(',', '.') + " VNĐ";
    }
}
