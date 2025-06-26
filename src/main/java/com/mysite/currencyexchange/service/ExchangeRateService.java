package com.mysite.currencyexchange.service;

import com.mysite.currencyexchange.dao.ExchangeRateDao;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateRequestDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.mapper.ExchangeRateMapper;
import com.mysite.currencyexchange.model.ExchangeRate;
import java.sql.SQLException;
import java.util.List;

public class ExchangeRateService {

    private final ExchangeRateDao exchangeRateDao;
    private final ExchangeRateMapper exchangeRateMapper;
    private final CurrencyService currencyService;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao, ExchangeRateMapper exchangeRateMapper,
                               CurrencyService currencyService) {
        this.exchangeRateDao = exchangeRateDao;
        this.exchangeRateMapper = exchangeRateMapper;
        this.currencyService = currencyService;
    }

    public List<ExchangeRateResponseDto> selectAllExchangeRates() throws SQLException {
        return exchangeRateDao.selectAllExchangeRates()
                .stream()
                .map(exchangeRateMapper::toExchangeRateResponseDto)
                .toList();
    }

    public CurrencyResponseDto selectCurrencyByCode(String code) throws SQLException {
        return currencyService.selectCurrencyByCode(code);
    }

    public ExchangeRateResponseDto saveCurrency(ExchangeRateRequestDto exchangeRateRequestDto,
                                                CurrencyResponseDto baseCurrencyResponseDto,
                                                CurrencyResponseDto targetCurrencyResponseDto) throws SQLException {
        ExchangeRate entity = new ExchangeRate(0, baseCurrencyResponseDto.getId(),
                targetCurrencyResponseDto.getId(), exchangeRateRequestDto.getRate());

        int generatedId = exchangeRateDao.saveExchangeRate(entity);

        return new ExchangeRateResponseDto(generatedId, baseCurrencyResponseDto,
                targetCurrencyResponseDto, exchangeRateRequestDto.getRate());
    }
}
