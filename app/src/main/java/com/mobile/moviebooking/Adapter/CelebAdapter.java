package com.mobile.moviebooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mobile.moviebooking.Entity.Celeb;
import com.mobile.moviebooking.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CelebAdapter extends RecyclerView.Adapter<CelebAdapter.CelebViewHolder> {
    private Context context;
    private List<Celeb> celebList;
    public CelebAdapter(Context context, List<Celeb> directorList) {
        this.context = context;
        this.celebList = directorList;
    }
    @NonNull
    @Override
    public CelebViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celeb_view,parent,false);
        return new CelebViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CelebViewHolder holder, int position) {
        Celeb celeb = celebList.get(position);
        holder.firstName.setText(celeb.getFirstName());
        holder.lastName.setText(celeb.getLastName());

        if (celeb.getLastName().length()+celeb.getFirstName().length() < 8) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(holder.constraintLayout);
            constraintSet.connect(R.id.firstName,ConstraintSet.BOTTOM,R.id.avatar,ConstraintSet.BOTTOM,0);
            constraintSet.connect(R.id.lastName,ConstraintSet.START,R.id.firstName,ConstraintSet.END,0);
            constraintSet.connect(R.id.lastName,ConstraintSet.TOP,R.id.firstName,ConstraintSet.TOP,0);
            constraintSet.connect(R.id.lastName,ConstraintSet.BOTTOM,R.id.firstName,ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(holder.constraintLayout);
            holder.lastName.setPadding(12,0,0,0);
        }

        Glide.with(context).load(celeb.getImageUrl()).into(holder.avatar);

        holder.fullLayout.setOnClickListener(v -> {
            String url = celeb.getInfoUrl();
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return celebList == null ? 0 : celebList.size();
    }
    public static class CelebViewHolder extends RecyclerView.ViewHolder {
        private TextView firstName;
        private TextView lastName;
        private CircleImageView avatar;
        private CardView fullLayout;
        private ConstraintLayout constraintLayout;

        public CelebViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.firstName);
            lastName = itemView.findViewById(R.id.lastName);
            avatar = itemView.findViewById(R.id.avatar);
            fullLayout = itemView.findViewById(R.id.fullLayout);
            constraintLayout = itemView.findViewById(R.id.parent_layout);
        }

    }
}
