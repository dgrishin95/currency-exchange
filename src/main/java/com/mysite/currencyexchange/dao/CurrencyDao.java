package com.mysite.currencyexchange.dao;

import com.mysite.currencyexchange.dao.base.BaseDao;
import com.mysite.currencyexchange.model.Currency;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao extends BaseDao {

    private static final String SELECT_FROM_CURRENCIES = "select * from currencies";
    private static final String SELECT_BY_CODE = "select * from currencies where code = ?";
    private static final String INSERT_INTO_CURRENCIES =
            "insert into currencies (name, code, sign) values (?, ?, ?)";

    public List<Currency> selectAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_CURRENCIES);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String sign = rs.getString("sign");
                currencies.add(new Currency(id, code, name, sign));
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
                String name = rs.getString("name");
                String sign = rs.getString("sign");

                return new Currency(id, foundCode, name, sign);
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
            preparedStatement.setString(1, currency.getName());
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
