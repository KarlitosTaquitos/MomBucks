package com.example.mombucks;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class MainActivity extends AppCompatActivity {

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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                registerBtn doRegister = new registerBtn();
                doRegister.execute("");
            }
        });

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
            if (userStr.trim().equals("") || passStr.trim().equals(""))
                z = "Please enter all fields";
            else {
                try {
                    Connection con = connection.CONN();
                    if (con == null)
                        z = "Please check your internet connection";
                    else {
                        try {
                            z = "That username is already in use";

                            String checkQ = "select * from users where username = '" + userStr + "';";
                            Statement checkS = con.createStatement();
                            ResultSet checkR = checkS.executeQuery(checkQ);

                            checkR.first();
                            if (checkR.getString("username").equals(userStr));
                        } catch (Exception x) {
                            String query = "insert into users values (DEFAULT, '" + userStr + "', '" + passStr + "');";
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
        String z = "";
        boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... string) {
            if (userStr.trim().equals("") || passStr.trim().equals(""))
                z = "Please enter all fields";
            else {
                try {
                    Connection con = connection.CONN();
                    if (con == null)
                        z = "Please check your internet connection";
                    else {
                        String query = "select * from users where username = '" + userStr + "';";
                        Log.e("QUERY", query);
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        rs.first();
                        if (rs.getString("password").equals(passStr))
                        { z = "Login Successful"; isSuccess = true; }

                        else z = "Password Incorrect";

                    }
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

            if (isSuccess)
                /*Go to next screen or something*/;

            progressDialog.hide();
        }
    }
}