package com.example.mombucks;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddMoney extends AppCompatActivity {
    Button backButton, depositButton;
    EditText depositEditText;
    String childName;
    double deposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_money);
        backButton = findViewById(R.id.cancelButton);
        depositButton = findViewById(R.id.depositButton);
        depositEditText = findViewById(R.id.depositEditText);
        childName = getIntent().getStringExtra("childName");
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChildProfileActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

                        depositMoneyTODB depositFunds = new depositMoneyTODB(childName, deposit);
                        depositFunds.execute();

                    } catch (Exception e1) {
                        // this means it is not double
                        e1.printStackTrace();
                    }
           }
        });

    }

    public class depositMoneyTODB extends AsyncTask<String, Double, String> {

        double deposit;
        String z = "", childName;

        public depositMoneyTODB(String childName, double deposit) {
            this.childName = childName;
            this.deposit = deposit;
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

                    /*SELECT
                            username,
                            password,
                            balance
                    FROM
                            users
                    WHERE
                            username = 'Bob';
                    update users
                    set
                            balance = balance + 10
                    where
                            username = "Bob";
                    SELECT
                            username,
                            password,
                            balance
                    FROM
                            users
                    WHERE
                            username = 'Bob';*/

                   /* String query = "select username, password, balance\n" +
                            "from users\n" +
                            "where username = '" + childName + "';\n" +
                            "update users\n" +
                            "set balance = balance + " + deposit + "\n" +
                            "where username = '" + childName + "';\n" +
                            "select username, password, balance\n" +
                            "from users\n" +
                            "where username = '" + childName + "';\n";*/

                   String query = "select * from users where username = '" + childName + "';";

                    System.out.print(query);
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    rs.first();
                    double bal = rs.getDouble("balance");
                    bal += deposit;
                    query = "update `users` set `balance` = '" + bal + "' where (`username` = '" + childName + "');";
                    stmt = con.createStatement();
                    stmt.executeUpdate(query);

                    z = "Money successfully added";
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
            if (z.equals("Money successfully added")) {
                Toast.makeText(AddMoney.this, "Money successfully added", Toast.LENGTH_SHORT).show();
                progressDialog.hide();

            }
        }
    }
}