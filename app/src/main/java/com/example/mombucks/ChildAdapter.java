package com.example.mombucks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {

    public Context context;
    public ArrayList<ChildData> childrenData;
    Picasso picasso;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView childName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.childImageView);
            childName = itemView.findViewById(R.id.childNameTextView);

        }
    }

    ChildAdapter(ArrayList<ChildData> childrenData, Context context) {
        this.childrenData = childrenData;
        this.context = context;
    }

    @NonNull
    @Override
    public ChildAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.child_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ChildAdapter.ViewHolder holder, int position) {
        ChildData childData = childrenData.get(position);
        holder.childName.setText(childData.getChildName());
        try {
            Glide.with(context)
                    .load(childData.getChildProfile())
                    .into(holder.circleImageView);
        } catch (Exception e) {
            Glide.with(context)
                    .load("https://images.freeimg.net/rsynced_images/childs-head-963144_1280.png")
                    .into(holder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return childrenData.size();
    }


}
