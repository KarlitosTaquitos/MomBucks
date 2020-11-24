package com.example.mombucks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class  MainActivity extends AppCompatActivity {

    ConnectionClass connection;
    EditText user, pass;
    Button register;
    Button login;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.button);
        login = (Button) findViewById(R.id.button2);

        connection = new ConnectionClass();
        progressDialog = new ProgressDialog(this);

        //Make listener for register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                registerBtn doRegister = new registerBtn();
                doRegister.execute("");
            }
        });

        //Make listener for login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginBtn doLogin = new loginBtn();
                doLogin.execute("");
            }
        });
    }

    public class registerBtn extends AsyncTask<String, String, String> {
        String userStr = user.getText().toString();
        String passStr = pass.getText().toString();
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... string) {

            //Check if they entered something in every field
            if (userStr.trim().equals("") || passStr.trim().equals(""))
                z = "Please enter all fields";
            else {
                try {

                    //Connect to the server
                    Connection con = connection.CONN();
                    if (con == null)
                        z = "Please check your internet connection";
                    else {
                        try {
                            z = "That username is already in use";

                            //Checks if the username they want to register is already in the database
                            String checkQ = "select * from users where username = '" + userStr + "';";
                            Statement checkS = con.createStatement();
                            ResultSet checkR = checkS.executeQuery(checkQ);

                            checkR.first();
                            //Throws an error if there's no match for the username
                            if (checkR.getString("username").equals(userStr));
                        } catch (Exception x) {

                            //If it throws that error, we make a new user
                            String query = "insert into users values (DEFAULT, DEFAULT, '" + userStr + "', '" + passStr + "', DEFAULT, 0,DEFAULT,0);";
                            System.out.print(query);
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                            z = "Register Successful";
                            isSuccess = true;
                        }
                    }
                } catch (Exception e) {
                    isSuccess = false;
                    z = "Exception " + e;
                }
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();

            progressDialog.hide();
        }
    }

    public class loginBtn extends AsyncTask<String, String, String> {
        String userStr = user.getText().toString();
        String passStr = pass.getText().toString();
        String accountType, moneys;
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... string) {

            //Checks if the user entered something in all fields
            if (userStr.trim().equals("") || passStr.trim().equals(""))
                z = "Please enter all fields";
            else {
                try {

                    //Connects to database
                    Connection con = connection.CONN();
                    if (con == null)
                        z = "Please check your internet connection";
                    else {

                        //Gets the user with provided username and tests the password
                        String query = "select * from users where username = '" + userStr + "';";
                        Log.e("QUERY", query);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        rs.first();
                        //Throws error if the username doesn't exist
                        if (rs.getString("password").equals(passStr)) {
                            accountType = rs.getString("account_type");
                            moneys = rs.getString("balance");
                            z = "Login Successful";
                            isSuccess = true;
                        } else z = "Password Incorrect";
                    }
                } catch (SQLException se) {
                    //Lets them know the username they entered isn't in the database
                    z = "That username doesn't exist";
                } catch (Exception e) {
                    isSuccess = false;
                    z = "Exception " + e;
                    Log.e("ERROR", z);
                }
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();
            progressDialog.hide();

            if (isSuccess) {
                /*Go to next screen or something*/
                Intent intent;
             if (accountType.equals("parent"))//im getting exception here
                    intent = new Intent(MainActivity.this, ParentView.class);
                else intent = new Intent(MainActivity.this, ChildLogInView.class);
                intent.putExtra("username", userStr);
                intent.putExtra("balance", moneys);
                startActivity(intent);
            }
        }
    }
}