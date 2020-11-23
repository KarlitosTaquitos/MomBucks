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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChoreAdapter extends RecyclerView.Adapter<ChoreAdapter.ViewHolder>{
    public Context context;
    public ArrayList<ChoreData> choresData;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //CircleImageView circleImageView;
        TextView choreName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //circleImageView = itemView.findViewById(R.id.imageView);
            choreName = itemView.findViewById(R.id.childNameTextView);

        }
    }

    ChoreAdapter(ArrayList<ChoreData> choresData, Context context) {
        this.choresData = choresData;
        this.context = context;
    }

    @NonNull
    @Override
    public ChoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chore_view, parent, false);
        ChoreAdapter.ViewHolder viewHolder = new ChoreAdapter.ViewHolder(v);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final ChoreAdapter.ViewHolder holder, int position) {
        final ChoreData data = choresData.get(position);
        holder.choreName.setText(data.getName());
        /*try {
            Glide.with(context)
                    .load(data.getDescription())
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
        });*/
    }

    @Override
    public int getItemCount() {
        return choresData.size();
    }

}
