package com.mysite.currencyexchange.dao.base;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BaseDao {
    private String url = "jdbc:sqlite:C:\\Work\\my\\_course\\currency-exchange\\db\\mydb.db";
    private String driverName = "org.sqlite.JDBC";

    protected Connection getConnection() {
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalStateException("Database connection failed", e);
        }
    }
}
