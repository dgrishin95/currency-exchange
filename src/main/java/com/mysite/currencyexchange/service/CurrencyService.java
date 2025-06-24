package com.mysite.currencyexchange.service;

import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.dto.CurrencyDto;
import com.mysite.currencyexchange.mapper.CurrencyMapper;
import com.mysite.currencyexchange.model.Currency;
import java.sql.SQLException;
import java.util.List;

public class CurrencyService {

    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyDao currencyDao, CurrencyMapper currencyMapper) {
        this.currencyDao = currencyDao;
        this.currencyMapper = currencyMapper;
    }

    public List<CurrencyDto> selectAllCurrencies() throws SQLException {
        return currencyDao
                .selectAllCurrencies()
                .stream()
                .map(currencyMapper::toDto)
                .toList();
    }

    public CurrencyDto selectCurrencyByCode(String code) throws SQLException {
        Currency currency = currencyDao.selectCurrencyByCode(code);
        return currency != null ? currencyMapper.toDto(currency) : null;
    }
}
