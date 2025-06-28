package com.mysite.currencyexchange.service;

import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.dto.CurrencyRequestDto;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.mapper.CurrencyMapper;
import com.mysite.currencyexchange.model.Currency;
import com.mysite.currencyexchange.service.base.BaseService;
import java.sql.SQLException;
import java.util.List;

public class CurrencyService extends BaseService {

    private final CurrencyDao currencyDao;
    private final CurrencyMapper currencyMapper;

    public CurrencyService(CurrencyDao currencyDao, CurrencyMapper currencyMapper) {
        this.currencyDao = currencyDao;
        this.currencyMapper = currencyMapper;
    }

    public List<CurrencyResponseDto> selectAllCurrencies() throws SQLException {
        return currencyDao
                .selectAllCurrencies()
                .stream()
                .map(currencyMapper::toCurrencyResponseDto)
                .toList();
    }

    public CurrencyResponseDto selectCurrencyByCode(String code) throws SQLException {
        Currency currency = currencyDao.selectCurrencyByCode(code.toUpperCase());
        return currency != null ? currencyMapper.toCurrencyResponseDto(currency) : null;
    }

    public CurrencyResponseDto saveCurrency(CurrencyRequestDto currencyRequestDto) throws SQLException {
        currencyRequestDto.setCode(currencyRequestDto.getCode().toUpperCase());

        Currency currency = currencyMapper.toEntity(currencyRequestDto);
        int generatedId = currencyDao.saveCurrency(currency);

        currency.setId(generatedId);
        return currencyMapper.toCurrencyResponseDto(currency);
    }

    public boolean currencyExistsByCode(String code) throws SQLException {
        return selectCurrencyByCode(code) != null;
    }

    public boolean isValidCurrencyRequest(CurrencyRequestDto dto) {
        return isNotBlank(dto.getName()) &&
                isNotBlank(dto.getCode()) &&
                isNotBlank(dto.getSign());

    }
}
