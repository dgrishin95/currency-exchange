package com.mysite.currencyexchange.service;

import com.mysite.currencyexchange.dao.ExchangeRateDao;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.dto.RawExchangeRateDto;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService {

    private final ExchangeRateDao exchangeRateDao;

    public ExchangeRateService(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    public List<ExchangeRateResponseDto> selectAllExchangeRates() throws SQLException {
        List<ExchangeRateResponseDto> exchangeRateResponseDtos = new ArrayList<>();
        List<RawExchangeRateDto> rawExchangeRateDtos = exchangeRateDao.selectAllExchangeRates();

        for (RawExchangeRateDto rawExchangeRateDto : rawExchangeRateDtos) {
            CurrencyResponseDto baseCurrencyResponseDto = new CurrencyResponseDto(
                    rawExchangeRateDto.getBaseId(), rawExchangeRateDto.getBaseCode(),
                    rawExchangeRateDto.getBaseName(), rawExchangeRateDto.getBaseSign());

            CurrencyResponseDto targetCurrencyResponseDto = new CurrencyResponseDto(
                    rawExchangeRateDto.getTargetId(), rawExchangeRateDto.getTargetCode(),
                    rawExchangeRateDto.getTargetName(), rawExchangeRateDto.getTargetSign());

            exchangeRateResponseDtos.add(new ExchangeRateResponseDto(
                    rawExchangeRateDto.getId(), baseCurrencyResponseDto,
                    targetCurrencyResponseDto, rawExchangeRateDto.getRate()
            ));
        }

        return exchangeRateResponseDtos;
    }
}
