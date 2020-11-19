package com.example.mombucks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
            circleImageView = itemView.findViewById(R.id.imageView);
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
    public void onBindViewHolder(@NonNull final ChildAdapter.ViewHolder holder, int position) {
        final ChildData childData = childrenData.get(position);
        holder.childName.setText(childData.getChildName());
        try {
            Glide.with(context)
                    .load(childData.getChildProfile())
                    .into(holder.circleImageView);
        } catch (Exception e) {
            Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            Glide.with(context)
                    .load("https://images.freeimg.net/rsynced_images/childs-head-963144_1280.png")
                    .into(holder.circleImageView);
        }
        holder.childName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), ChildProfileActivity.class);
                holder.itemView.getContext().startActivity(intent
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)//setFlags creates a new task
                        .putExtra("childName", childData.childName)
                        .putExtra("childBalance", childData.childProfile));
            }
        });
    }

    @Override
    public int getItemCount() {
        return childrenData.size();
    }


}
