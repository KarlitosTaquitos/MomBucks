package com.mombuck2.addchild113;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass2 {


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
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            //  System.out.println("ERRO "+e.getLocalizedMessage());
        }

        return conn;
    }
}
