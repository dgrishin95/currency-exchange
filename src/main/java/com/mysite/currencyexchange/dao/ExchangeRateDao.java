package com.mysite.currencyexchange.dao;

import com.mysite.currencyexchange.dao.base.BaseDao;
import com.mysite.currencyexchange.dto.RawExchangeRateDto;
import com.mysite.currencyexchange.model.ExchangeRate;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDao extends BaseDao {

    private static final String SELECT_FROM_EXCHANGE_RATES = "select er.id, \n" +
            "       bc.id as base_id, bc.code as base_code, bc.name as base_name, bc.sign as base_sign,\n" +
            "       tc.id as target_id, tc.code as target_code, tc.name as target_name, tc.sign as target_sign,\n" +
            "       er.rate\n" +
            "from exchange_rates er\n" +
            "join currencies bc on er.basecurrencyid = bc.id\n" +
            "join currencies tc on er.targetcurrencyid = tc.id;";

    private static final String INSERT_INTO_EXCHANGE_RATES =
            "insert into exchange_rates (basecurrencyid, targetcurrencyid, rate) values (?, ?, ?)";

    public List<RawExchangeRateDto> selectAllExchangeRates() throws SQLException {
        List<RawExchangeRateDto> rawExchangeRateDtos = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_EXCHANGE_RATES);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");

                int baseId = rs.getInt("base_id");
                String baseCode = rs.getString("base_code");
                String baseName = rs.getString("base_name");
                String baseSign = rs.getString("base_sign");

                int targetId = rs.getInt("target_id");
                String targetCode = rs.getString("target_code");
                String targetName = rs.getString("target_name");
                String targetSign = rs.getString("target_sign");

                BigDecimal rate = rs.getBigDecimal("rate");

                rawExchangeRateDtos.add(new RawExchangeRateDto(id, baseId, baseCode, baseName, baseSign,
                        targetId, targetCode, targetName, targetSign, rate));
            }

        }

        return rawExchangeRateDtos;
    }

    public int saveExchangeRate(ExchangeRate exchangeRate) throws SQLException {
        int id = 0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTO_EXCHANGE_RATES,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }
        }

        return id;
    }
}