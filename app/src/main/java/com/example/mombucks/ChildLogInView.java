package com.example.mombucks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ChildLogInView extends AppCompatActivity {


    ArrayList<ChoreData> itemChoreData = new ArrayList<>();
    ChoreAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ChildData data;

    TextView name, mone;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_log_in_view);

        name = findViewById(R.id.username);
        mone = findViewById(R.id.mone);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        layoutManager = new LinearLayoutManager(getApplicationContext());

        data = new ChildData(getIntent().getStringExtra("username"), getIntent().getStringExtra("balance"));
        name.setText(data.getChildName());
        mone.setText("$" + data.getChildProfile());

        progressDialog = new ProgressDialog(this);

        RetrieveChoreData retrieve = new RetrieveChoreData();
        retrieve.execute();
    }

    public class RetrieveChoreData extends AsyncTask<String, String, String> {
        ConnectionClass connection = new ConnectionClass();
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... String) {
            try {
                //Connect to the server
                Connection con = connection.CONN();
                if (con == null)
                    z = "Please check your internet connection";
                else {
                    try {
                        z = "Username not found";

                        //here instead of DEFAULT add parent name here after integrating login activity (IMPORTANT!!!)

                        String checkQ = "SELECT chore,description FROM chores WHERE username='" + data.getChildName() + "';";//this is the query to retrieve values from DB
                        Statement checkS = con.createStatement();
                        ResultSet checkR = checkS.executeQuery(checkQ);
                        z = "checkR == null";
                        if (checkR != null) {

                            while (checkR.next()) {
                                itemChoreData.add(new ChoreData(checkR.getString("chore"), checkR.getString("description")));
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

            adapter = new ChoreAdapter(itemChoreData, getApplicationContext(), data);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
}