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

    private static final String SELECT =
            "select er.id, \n" +
            "       bc.id as base_id, bc.code as base_code, bc.name as base_name, bc.sign as base_sign,\n" +
            "       tc.id as target_id, tc.code as target_code, tc.name as target_name, tc.sign as target_sign,\n" +
            "       er.rate\n" +
            "from exchange_rates er\n" +
            "join currencies bc on er.basecurrencyid = bc.id\n" +
            "join currencies tc on er.targetcurrencyid = tc.id;";

    private static final String INSERT =
            "insert into exchange_rates (basecurrencyid, targetcurrencyid, rate) values (?, ?, ?)";

    private static final String SELECT_BY_CODES_IDS =
            "select * from exchange_rates where basecurrencyid = ? and targetcurrencyid = ?";

    private static final String UPDATE =
            "update exchange_rates set rate = ? where basecurrencyid = ? and targetcurrencyid = ?";

    private static final String SELECT_BY_USD_CODE_AND_TARGET_ID =
            "select er.rate\n" +
                    "from exchange_rates er\n" +
                    "join currencies bc on er.basecurrencyid = bc.id\n" +
                    "join currencies tc on er.targetcurrencyid = tc.id\n" +
                    "where bc.code = 'USD' and tc.id = ?";

    public List<RawExchangeRateDto> selectAllExchangeRates() throws SQLException {
        List<RawExchangeRateDto> rawExchangeRateDtos = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT);
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
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT,
                     Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, exchangeRate.getBaseCurrencyId());
            preparedStatement.setInt(2, exchangeRate.getTargetCurrencyId());
            preparedStatement.setBigDecimal(3, exchangeRate.getRate());

            preparedStatement.executeUpdate();

            try (ResultSet rs = preparedStatement.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        }

        return id;
    }

    public ExchangeRate selectExchangeRateByCodesIds(int baseCurrencyId, int targetCurrencyId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_CODES_IDS)) {
            preparedStatement.setInt(1, baseCurrencyId);
            preparedStatement.setInt(2, targetCurrencyId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                BigDecimal rate = rs.getBigDecimal("rate");

                return new ExchangeRate(id, baseCurrencyId, targetCurrencyId, rate);
            } else {
                return null;
            }
        }
    }

    public BigDecimal selectRateByUsdAndTargetId(int targetCurrencyId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_USD_CODE_AND_TARGET_ID)) {
            preparedStatement.setInt(1, targetCurrencyId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("rate");
            } else {
                return null;
            }
        }
    }

    public boolean updateExchangeRate(BigDecimal rate, int baseCurrencyId, int targetCurrencyId) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {
            preparedStatement.setBigDecimal(1, rate);
            preparedStatement.setInt(2, baseCurrencyId);
            preparedStatement.setInt(3, targetCurrencyId);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        }
    }
}