package com.example.mombucks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AddChores extends AppCompatActivity {
    Button backButton, applyButton;
    EditText descriptionEditText;
    String childName, chore, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chores);
        backButton = findViewById(R.id.cancelButton);
        applyButton = findViewById(R.id.applyButton);
        Spinner dropdown = findViewById(R.id.spinner1);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        childName = getIntent().getStringExtra("childName");
        //this is used to collect the passed string .getStringlkExtra collects putExtra
        String[] items = new String[]{"Bedroom", "Bathroom", "Kitchen", "Living Room", "Dinner Table", "Laundry", "Yard", "Pets", "Homework", "Trash", "Car", "Music", "Sports"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChildProfileActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        //when user clicks apply button we get the selected item in drop don and the description
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chore = dropdown.getSelectedItem().toString();
                description = descriptionEditText.getText().toString();
                addChore(chore, description);//here i am passing those two strings this method
            }
        });

    }


    private void addChore(String chore, String choreDescription) {
        //these are those 3 variables
        AddChoreTODB addChores = new AddChoreTODB(childName, choreDescription, chore);//here iam creating a object
        addChores.execute();
    }

    // constructor is pipeline between class we can pass variable
//    through constructor here iam passing those 3 variable
    public class AddChoreTODB extends AsyncTask<String, String, String> {
        public AddChoreTODB(String childName, String description, String chore) {
            this.childName = childName;//here local variables are assigned from the passed variables
            this.description = description;
            this.chore = chore;
        }

        String z = "", childName, description, chore;
        //we shloud assign these variable from the variable passed through constructor
        boolean isSuccess = false;

        ProgressDialog progressDialog = new ProgressDialog(AddChores.this); //this is a loading dialog class

        ConnectionClass connection = new ConnectionClass();//connection class is were the magic happens

        //here we update data to database from here it is very important
        @Override
        protected void onPreExecute() {//before updating to database we show a loading dialog
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        //in line 68 u see AsyncTask that is a type of thread it has 3 methods
        //preExecute doInBackgroud and postExecute
        //preExecute: The things which should Execute before DoInBAckgound are written here
        //doInBackground: The thing should actually hapeens are written here
        // postExecute The things which should Execute after DoInBAckgound are written here

        @Override
        protected String doInBackground(String... string) {
            try {

                //Connect to the server
                Connection con = connection.CONN();//here we connect to your sql server

                if (con == null)//if con is null then we can assume that there
                    // is no internet but it can be any other reasons

                    z = "Please check your internet connection";
                else {


              //we do that here but programmatically
                    //below line is equal to this query insert into chore values(DEAFULT,vanessa,kitchen,take out the trrash, false);
                    
                    String query = "insert into chores values (0,  '" + childName + "','" + chore + "','" + description + "',false);";
                    System.out.print(query);
                    Statement stmt = con.createStatement();//here we create a statement where we can Execute the query
                    stmt.executeUpdate(query);//these two lines are responsible for executing queries

                    z = "Chore Successfully add";
                    isSuccess = true;

                }

                return z;
            } catch (SQLException throwables) {
                isSuccess = false;
                z = "Exception " + throwables;
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {//here after completing the doInBackground we give a message to user whether the data is upadted or not


             Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();
            if (z.equals("chore Successfully add")) {
                Toast.makeText(AddChores.this, "chore Successfully add", Toast.LENGTH_SHORT).show();
                progressDialog.hide();

            }
        }
    }
}
