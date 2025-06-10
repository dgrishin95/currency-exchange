package com.mysite.currencyexchange.dao;

import com.mysite.currencyexchange.model.Currency;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    private String url = "jdbc:sqlite::resource:db/mydb.db";
    private String driverName = "org.sqlite.JDBC";

    private static final String SELECT_FROM_CURRENCIES = "select * from currencies";

    protected Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName(driverName);
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public List<Currency> selectAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_CURRENCIES)) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String fullName = rs.getString("fullname");
                String sign = rs.getString("sign");
                currencies.add(new Currency(id, code, fullName, sign));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }

        return currencies;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
