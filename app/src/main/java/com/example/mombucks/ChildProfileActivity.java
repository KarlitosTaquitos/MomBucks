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
    Button backButton, addChoresButton, depositButton;
    TextView childNameTextView, childBalanceTextView;
    String childName, childBalance, childProfile, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);

        childProfileImageView = findViewById(R.id.childImageView);
        backButton = findViewById(R.id.backButton);
        addChoresButton = findViewById(R.id.addChoresButton);
        depositButton = findViewById(R.id.depositButton);
        childNameTextView = findViewById(R.id.childNameTextView);
        childBalanceTextView = findViewById(R.id.childBalanceTextView);

        //get the passed child data from parent activity.
        childName = getIntent().getStringExtra("childName");
        childBalance = getIntent().getStringExtra("childBalance");
        childProfile = getIntent().getStringExtra("childProfile");
        username = getIntent().getStringExtra("username");

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
                Intent intent = new Intent(getApplicationContext(), ParentView.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra("username", username);
                startActivity(intent);
            }
        });
        addChoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//.putExtra passes that string childname to addchroes activty
                //putExtra is used to pass string one axtivty to other activit
                Intent intent = new Intent(getApplicationContext(), AddChores.class)
                        .putExtra("childName", childName)
                        .putExtra("username", username);
                startActivity(intent);

            }
        });
        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMoney.class)
                        .putExtra("childName", childName)
                        .putExtra("username", username);
                startActivity(intent);

            }
        });

    }

}