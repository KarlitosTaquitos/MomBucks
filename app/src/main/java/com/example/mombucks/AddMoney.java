package com.example.mombucks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddMoney extends AppCompatActivity {
    Button backButton, depositButton, addFundsButton;
    TextView currentFundsText;
    EditText depositEditText, addFundsEditText;
    String childName, username;
    double deposit, funds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        backButton = findViewById(R.id.cancelButton);
        depositButton = findViewById(R.id.depositButton);
        depositEditText = findViewById(R.id.depositEditText);
        currentFundsText = findViewById(R.id.currentFundsText);
        addFundsButton = findViewById(R.id.addMoneyButton);
        addFundsEditText = findViewById(R.id.addFundsEditText);
        childName = getIntent().getStringExtra("childName");
        username = getIntent().getStringExtra("username");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChildProfileActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .putExtra("username", username);
                startActivity(intent);
            }
        });

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!depositEditText.equals("")) // make sure that depositEditText isn't empty
                    try
                    {
                        deposit = Double.parseDouble(depositEditText.getText().toString());
                        // it means it is double

                        depositMoneyTODB depositFunds = new depositMoneyTODB(childName, deposit, username);
                        depositFunds.execute();

                    } catch (Exception e1) {
                        // this means it is not double
                        e1.printStackTrace();
                    }
           }
        });

        addFundsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(!addFundsEditText.equals("")) // make sure that depositEditText isn't empty
                    try
                    {
                        funds = Double.parseDouble(addFundsEditText.getText().toString());
                        // it means it is double

                        addFundsTODB addFunds = new addFundsTODB(username, funds);
                        addFunds.execute();

                    } catch (Exception e1) {
                        // this means it is not double
                        e1.printStackTrace();
                    }
            }

        });

    }
    public class addFundsTODB extends AsyncTask<String, Double, String> {

        double funds;
        String z = "", username;

        public addFundsTODB(String username, double funds) {
            this.username = username;
            this.funds = funds;
        }
        boolean isSuccess = false;

        ProgressDialog progressDialog = new ProgressDialog(AddMoney.this);

        ConnectionClass connection = new ConnectionClass();
        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... string) {
            try {

                Connection con = connection.CONN();

                if (con == null)

                    z = "Please check your internet connection";
                else {

                    String query = "select * from users where username = '" + username + "';";

                    System.out.print(query);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    rs.first();
                    double parentBal = rs.getDouble("balance");
                    parentBal += funds;

                    query = "update `users` set `balance` = '" + parentBal + "' where (`username` = '" + username + "');";
                    stmt = con.createStatement();
                    stmt.executeUpdate(query);
                    z = "Funds successfully added";
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
        protected void onPostExecute(String s) {


            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();
            if (z.equals("Funds successfully added")) {
                Toast.makeText(AddMoney.this, "Funds successfully added", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }
    }

    public class depositMoneyTODB extends AsyncTask<String, Double, String> {

        double deposit;
        String z = "", childName, username;

        public depositMoneyTODB(String childName, double deposit, String username) {
            this.childName = childName;
            this.deposit = deposit;
            this.username = username;
        }
        boolean isSuccess = false;

        ProgressDialog progressDialog = new ProgressDialog(AddMoney.this);

        ConnectionClass connection = new ConnectionClass();

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... string) {
            try {

                Connection con = connection.CONN();

                if (con == null)

                    z = "Please check your internet connection";
                else {

                   String query = "select * from users where username = '" + childName + "';";

                    System.out.print(query);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    rs.first();
                    double childBal = rs.getDouble("balance");
                    String query2 = "select * from users where username = '" + username + "';";
                    System.out.print(query2);
                    Statement stmt2 = con.createStatement();
                    ResultSet rs2 = stmt2.executeQuery(query2);
                    double parentBal = rs2.getDouble("balance");
                    if (parentBal < deposit){
                        z = "Deposit unsuccessful. Not enough funds.";
                        isSuccess = false;
                    }

                    else if (parentBal >= deposit)
                    {
                        childBal += deposit;
                        query = "update `users` set `balance` = '" + childBal + "' where (`username` = '" + childName + "');";
                        stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        z = "Money successfully added";
                        isSuccess = true;
                        parentBal -= deposit;
                        query2 = "update `users` set `balance` = '" + parentBal + "' where (`username` = '" + username + "');";
                        stmt2 = con.createStatement();
                        stmt2.executeUpdate(query2);
                    }

                }

                return z;
            } catch (SQLException throwables) {
                isSuccess = false;
                z = "Exception " + throwables;
            }
            return z;
        }
        @Override
        protected void onPostExecute(String s) {


            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();
            if (z.equals("Money successfully added")) {
                Toast.makeText(AddMoney.this, "Money successfully added", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
            else if (z.equals("Deposit unsuccessful. Not enough funds.")){
                Toast.makeText(AddMoney.this, "Deposit unsuccessful. Not enough funds.", Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }
    }
}