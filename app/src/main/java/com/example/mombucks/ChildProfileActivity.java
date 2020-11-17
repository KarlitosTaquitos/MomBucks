package com.example.mombucks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ChildProfileActivity extends AppCompatActivity {
    ImageView childProfileImageView;
    Button backButton, addChoresButton;
    TextView childNameTextView, childBalanceTextView;
    String childName, childBalance, childProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);

        childProfileImageView = findViewById(R.id.childImageView);
        backButton = findViewById(R.id.backButton);
        addChoresButton = findViewById(R.id.addChoresButton);
        childNameTextView = findViewById(R.id.childNameTextView);
        childBalanceTextView = findViewById(R.id.childBalanceTextView);

        //get the passed child data from main activity.
        childName = getIntent().getStringExtra("childName");
        childBalance = getIntent().getStringExtra("childBalance");
        childProfile = getIntent().getStringExtra("childProfile");

        childNameTextView.setText(childName);
        childBalanceTextView.setText("$" + childBalance);

        try {
            Glide.with(getApplicationContext())
                    .load(childProfile)
                    .into(childProfileImageView);
            System.out.println(childProfile);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
            Glide.with(getApplicationContext())
                    .load("https://images.freeimg.net/rsynced_images/childs-head-963144_1280.png")
                    .into(childProfileImageView);
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        addChoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddChores.class).putExtra("childName", childName);
                startActivity(intent);

            }
        });

    }

}