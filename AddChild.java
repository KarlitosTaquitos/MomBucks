package com.mombuck.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.IllegalFormatConversionException;
import java.util.List;
import java.util.PropertyResourceBundle;

public class AddChild extends AppCompatActivity {

    Button cancelButton, saveButton;
    public static EditText userNameEditText, weeklyAllowanceEditText;
    String childName, weeklyAllowance;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        cancelButton = findViewById(R.id.canceButton);
        saveButton = findViewById(R.id.saveButton);
        weeklyAllowanceEditText = findViewById(R.id.editTextNumberDecimal);
        userNameEditText = findViewById(R.id.editTextTextPersonName);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    childName = userNameEditText.getText().toString();
                    weeklyAllowance = weeklyAllowanceEditText.getText().toString();

                    if (childName.isEmpty()) {
                        userNameEditText.setError("Please enter a name");
                    } else if (weeklyAllowance.isEmpty() || (Integer.parseInt(weeklyAllowance) == 0)) {
                        weeklyAllowanceEditText.setError("Please enter a valid amount");
                    } else {
                        updateToDatabase();
                    }


                } catch (IllegalFormatConversionException e) {
                    weeklyAllowanceEditText.setError("Enter a valid amount");
                } catch (Exception ignored) {
                    //do nothing
                }


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddChild.this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }

    void updateToDatabase() {
        AddChildDB addChildDb = new AddChildDB(childName, weeklyAllowance, this);
        addChildDb.execute();
    }


    public class AddChildDB extends AsyncTask<String, String, String> {
        public AddChildDB(String childName, String weeklyAllowance, Context context) {
            this.childName = childName;
            this.weeklyAllowance = weeklyAllowance;
            this.context = context;
        }

        private Context context;
        ProgressDialog progressDialog = new ProgressDialog(context);
        ConnectionClass connection = new ConnectionClass();
        String childName, weeklyAllowance;
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
                        z = "That Child is already been added";

                        //Checks if the child they want to add is already in the database
                        String checkQ = "select * from users where username = '" + childName + "';";
                        Statement checkS = con.createStatement();
                        ResultSet checkR = checkS.executeQuery(checkQ);

                        checkR.first();
                        //Throws an error if there's no match for the child name
                        if (checkR.getString("username").equals(childName)) ;
                    } catch (Exception x) {

                        //If it throws that error, we make a new child profile
                        String query = "insert into users values (DEFAULT, DEFAULT, '" + childName + "', 'DEFAULT ', " +
                                "DEFAULT,  '" + weeklyAllowance + "'";
                        System.out.print(query);
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);

                        z = "Child Successfully add";
                        isSuccess = true;
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
            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();

            progressDialog.hide();
        }
    }
}



