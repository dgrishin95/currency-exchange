package com.mysite.currencyexchange.test;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    public ConnectDB() {
        // connection string
        var url = "jdbc:sqlite::resource:db/mydb.db";

        try {
            Class.forName("org.sqlite.JDBC");
            var conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
