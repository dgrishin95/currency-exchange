package com.mysite.currencyexchange.service;

import com.mysite.currencyexchange.dao.ExchangeRateDao;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.mapper.ExchangeRateMapper;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRateService {

    private final ExchangeRateDao exchangeRateDao;
    private final ExchangeRateMapper exchangeRateMapper;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, ExchangeRateMapper exchangeRateMapper) {
        this.exchangeRateDao = exchangeRateDao;
        this.exchangeRateMapper = exchangeRateMapper;
    }

    public List<ExchangeRateResponseDto> selectAllExchangeRates() throws SQLException {
        return exchangeRateDao.selectAllExchangeRates()
                .stream()
                .map(exchangeRateMapper::toExchangeRateResponseDto)
                .toList();
    }
}
