package com.mysite.currencyexchange.dao;

import com.mysite.currencyexchange.model.Currency;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    private String url = "jdbc:sqlite:C:\\Work\\my\\_course\\currency-exchange\\db\\mydb.db";
    private String driverName = "org.sqlite.JDBC";

    private static final String SELECT_FROM_CURRENCIES = "select * from currencies";
    private static final String SELECT_BY_CODE = "select * from currencies where code = ?";
    private static final String INSERT_INTO_CURRENCIES =
            "insert into currencies (code, fullname, sign) values (?, ?, ?)";

    protected Connection getConnection() {
        try {
            Class.forName(driverName);
            return DriverManager.getConnection(url);
        } catch (SQLException | ClassNotFoundException e) {
            throw new IllegalStateException("Database connection failed", e);
        }
    }

    public List<Currency> selectAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_CURRENCIES);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String fullName = rs.getString("fullname");
                String sign = rs.getString("sign");
                currencies.add(new Currency(id, code, fullName, sign));
            }

        }

        return currencies;
    }

    public Currency selectCurrencyByCode(String code) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODE)) {
            preparedStatement.setString(1, code);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                String foundCode = rs.getString("code");
                String fullName = rs.getString("fullname");
                String sign = rs.getString("sign");

                return new Currency(id, foundCode, fullName, sign);
            } else {
                return null;
            }
        }
    }

    public int saveCurrency(Currency currency) throws SQLException {
        int id = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_CURRENCIES,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, currency.getFullName());
            preparedStatement.setString(2, currency.getCode());
            preparedStatement.setString(3, currency.getSign());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }
}
