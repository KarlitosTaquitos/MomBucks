package com.example.mombucks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ParentView extends AppCompatActivity {
    public Button addChildButton;
    ArrayList<ChildData> itemChildData = new ArrayList<>();//here itemChildData is a array object of childData now we have to add values using add method
    ChildAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_view);
        addChildButton = (Button) findViewById(R.id.button);
        //recyclerView  is used when u have something(data) that is to be 
        // updated to ui(user interface like buttons textview spinner etc) in a list manner 
        //so in our case we retrive child name and profile link and store in a array 
        // and then we display those data in textview and image view using recyler view 

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(getApplicationContext());

        username = getIntent().getStringExtra("username");
        
        addChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParentView.this, AddChild.class);
                startActivity(intent);
            }
        });
        RetrieveChildData retrieveChildData = new RetrieveChildData();
        retrieveChildData.execute();

    }

    public class RetrieveChildData extends AsyncTask<String, String, String> {


        ProgressDialog progressDialog = new ProgressDialog(ParentView.this);
        ConnectionClass connection = new ConnectionClass();
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }


        @Override
        protected String doInBackground(String... string) {
            try {
                //Connect to the server
                Connection con = connection.CONN();
                if (con == null)
                    z = "Please check your internet connection";
                else {
                    try {
                        z = "Username not found";

                        //here instead of DEFAULT add parent name here after integrating login activity (IMPORTANT!!!)

                        String checkQ = "SELECT username,money FROM users WHERE parent='" + username + "';";//this is the query to retrieve values from DB
                        Statement checkS = con.createStatement();
                        ResultSet checkR = checkS.executeQuery(checkQ);
                        z = "checkR == null";
                        if (checkR != null) {

                            while (checkR.next()) {

                                itemChildData.add(new ChildData(checkR.getString("username")
                                        , checkR.getString("money")));
                            }
                            z = "Data found";
                        }

                    } catch (SQLException throwable) {
                        throwable.printStackTrace();
                        System.out.println(throwable.getMessage());
                    }
                }
            } catch (Exception e) {
                isSuccess = false;
                z = "Exception " + e;
            }
            return z;
        }


        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();
           //can u see that itemChildData that is array
            adapter = new ChildAdapter(itemChildData, getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

        }


    }
}

