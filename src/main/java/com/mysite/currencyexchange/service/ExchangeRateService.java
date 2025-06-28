package com.mysite.currencyexchange.service;

import com.mysite.currencyexchange.dao.ExchangeRateDao;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateRequestDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.mapper.ExchangeRateMapper;
import com.mysite.currencyexchange.model.ExchangeRate;
import java.math.BigDecimal;
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

    public ExchangeRateResponseDto saveExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto,
                                                    CurrencyResponseDto baseCurrencyResponseDto,
                                                    CurrencyResponseDto targetCurrencyResponseDto) throws SQLException {
        ExchangeRate entity = new ExchangeRate(0, baseCurrencyResponseDto.getId(),
                targetCurrencyResponseDto.getId(), exchangeRateRequestDto.getRate());

        int generatedId = exchangeRateDao.saveExchangeRate(entity);

        return new ExchangeRateResponseDto(generatedId, baseCurrencyResponseDto,
                targetCurrencyResponseDto, exchangeRateRequestDto.getRate());
    }

    public ExchangeRateResponseDto selectExchangeRateByCurrenciesCodes(CurrencyResponseDto baseCurrencyResponseDto,
                                                                       CurrencyResponseDto targetCurrencyResponseDto) throws SQLException {
        ExchangeRate exchangeRate = exchangeRateDao.selectExchangeRateByCodesIds(
                baseCurrencyResponseDto.getId(), targetCurrencyResponseDto.getId());

        return exchangeRate != null ?
                new ExchangeRateResponseDto(exchangeRate.getId(), baseCurrencyResponseDto,
                        targetCurrencyResponseDto, exchangeRate.getRate())
                : null;
    }

    public boolean updateExchangeRate(BigDecimal rate, CurrencyResponseDto baseCurrencyResponseDto,
                                      CurrencyResponseDto targetCurrencyResponseDto) throws SQLException {
        return exchangeRateDao.updateExchangeRate(
                rate, baseCurrencyResponseDto.getId(), targetCurrencyResponseDto.getId());
    }
}
