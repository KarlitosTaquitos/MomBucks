package com.example.mombucks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChildDescription extends AppCompatActivity {
    Button backButton;
    TextView choreNameTextView, descriptionNameTextView, name;
    String choreName, description, username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_description);
        backButton = findViewById(R.id.backButton);
        choreNameTextView = findViewById(R.id.choreNameTextView);
        descriptionNameTextView = findViewById(R.id.descriptionNameTextView);
        name = findViewById(R.id.username);

        choreName = getIntent().getStringExtra("choreName");
        description = getIntent().getStringExtra("description");
        username = getIntent().getStringExtra("username");

        choreNameTextView.setText(choreName);
        descriptionNameTextView.setText(description);



        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChildDescription.this, ChildLogInView.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

    }
}