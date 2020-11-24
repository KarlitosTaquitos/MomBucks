package com.example.mombucks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.IllegalFormatConversionException;

public class AddChild extends AppCompatActivity {

    Button cancelButton, saveButton, imageButton;
    EditText userNameEditText, weeklyAllowanceEditText,childPasswordEditText;
    String childName, weeklyAllowance, childPassword, username,
            imageurl1 = "https://images.assetsdelivery.com/compings_v2/yupiramos/yupiramos1705/yupiramos170531607.jpg",
            imageurl2 = "https://images.freeimg.net/rsynced_images/childs-head-963144_1280.png",
            imageurl3 = "https://cdn.pixabay.com/photo/2020/10/04/20/05/boy-5627460_1280.png",
            imageUrl = imageurl2;
    ImageView imgView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        imgView = findViewById(R.id.childImageView);

        Glide.with(AddChild.this)
                .load(imageUrl)
                .into(imgView);

        username = getIntent().getStringExtra("username");

        imageButton = findViewById(R.id.changeButton);
        cancelButton = findViewById(R.id.canceButton);
        saveButton = findViewById(R.id.saveButton);
        weeklyAllowanceEditText = findViewById(R.id.editTextNumberDecimal);
        userNameEditText = findViewById(R.id.editTextTextPersonName);
        childPasswordEditText = findViewById(R.id.editTextTextPersonName2);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUrl.equals(imageurl1)) {
                    imageUrl = imageurl2;
                    Glide.with(AddChild.this)
                            .load(imageUrl)
                            .into(imgView);
                } else if (imageUrl.equals(imageurl2)) {
                    imageUrl = imageurl3;
                    Glide.with(AddChild.this)
                            .load(imageUrl)
                            .into(imgView);
                } else if (imageUrl.equals(imageurl3)) {
                    imageUrl = imageurl1;
                    Glide.with(AddChild.this)
                            .load(imageUrl)
                            .into(imgView);
                }
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    childName = userNameEditText.getText().toString();
                    weeklyAllowance = weeklyAllowanceEditText.getText().toString();
                    childPassword = childPasswordEditText.getText().toString();

                    if (childName.isEmpty()) {
                        userNameEditText.setError("Please enter a name");
                    } else if (weeklyAllowance.isEmpty() || (Integer.parseInt(weeklyAllowance) == 0)) {
                        weeklyAllowanceEditText.setError("Please enter a valid amount");
                    }else if (childPassword.isEmpty()){
                        childPasswordEditText.setError("Please enter a password ");
                    } else {
                        updateToDatabase();
                    }


                } catch (IllegalFormatConversionException e) {
                    weeklyAllowanceEditText.setError("Enter a valid amount");

                } catch (Exception e) {
                    childPasswordEditText.setError("Enter a password");

                }


            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddChild.this, ParentView.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("username", username);
                startActivity(intent);

            }
        });

    }

    void updateToDatabase() {
        AddChildDB addChildDb = new AddChildDB(childName, weeklyAllowance, childPassword);
        addChildDb.execute();
    }


    public class AddChildDB extends AsyncTask<String, String, String> {

        public AddChildDB(String childName, String weeklyAllowance, String childPassword) {
            this.childName = childName;
            this.weeklyAllowance = weeklyAllowance;
            this.childPassword = childPassword;

        }


        ProgressDialog progressDialog = new ProgressDialog(AddChild.this);
        ConnectionClass connection = new ConnectionClass();
        String childName, weeklyAllowance, childPassword;
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
                        z = "That Child has already been added";
                        System.out.println(childName + " " + weeklyAllowance+ " " +childPassword);
                        //Checks if the username they want to register is already in the database
                        String checkQ = "select * from users where username = '" + childName + "';";
                        Statement checkS = con.createStatement();
                        ResultSet checkR = checkS.executeQuery(checkQ);

                        checkR.first();
                        Toast.makeText(getApplicationContext(),"user already exist", Toast.LENGTH_LONG).show();
                        //Throws an error if there's no match for the username
                        if (checkR.getString("username").equals(childName)) ;
                    } catch (Exception x) {

                        //If it throws that error, we make a new user
                        //Add the parent's username here in parent field

                        //TODO:Add the parent's username here in parent field
                        String query = "insert into users values (DEFAULT, 'child', '" + childName + "', '"+ childPassword +
                                "', '" + username + "','" + weeklyAllowance + "','" + imageUrl + "','" + weeklyAllowance + "' );";
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

            if (z.equals("Child Successfully add")) {
                startActivity(new Intent(getApplicationContext(), ParentView.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("username", username));
            }
        }
    }
}
