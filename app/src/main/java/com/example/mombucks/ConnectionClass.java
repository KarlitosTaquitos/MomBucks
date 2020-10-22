package com.example.mombucks;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    String classs = "com.mysql.jdbc.Driver";

    String url = "jdbc:mysql://34.121.163.145:3306/test";
    String usnm = "root";
    String pswd = "MommyMoney";

    @SuppressLint("NewApi")
    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Connection conn = null;
        String ConnURL = null;

        try {
            Class.forName(classs);

            conn = DriverManager.getConnection(url, usnm, pswd);

            //conn = DriverManager.getConnection(ConnURL);
        } catch (SQLException se) {
            Log.e("ERRO", se.getMessage());
        } catch (Exception e) {
            Log.e("ERRO2", e.getMessage());
        }

        return conn;
    }
}
