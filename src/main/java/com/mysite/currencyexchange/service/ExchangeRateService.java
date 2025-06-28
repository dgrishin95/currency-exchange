package com.mysite.currencyexchange.service;

import com.mysite.currencyexchange.dao.ExchangeRateDao;
import com.mysite.currencyexchange.dto.CurrencyPairDto;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateRequestDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.mapper.ExchangeRateMapper;
import com.mysite.currencyexchange.model.ExchangeRate;
import com.mysite.currencyexchange.service.base.BaseService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ExchangeRateService extends BaseService {

    private final ExchangeRateDao exchangeRateDao;
    private final ExchangeRateMapper exchangeRateMapper;
    private final CurrencyService currencyService;

    protected static final int CURRENCY_CODE_LENGTH = 3;
    protected static final int CURRENCY_CODES_LENGTH = 6;

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
                                                    CurrencyPairDto currencyPairDto) throws SQLException {
        ExchangeRate entity = new ExchangeRate(0, currencyPairDto.getBaseCurrency().getId(),
                currencyPairDto.getTargetCurrency().getId(), exchangeRateRequestDto.getRate());

        int generatedId = exchangeRateDao.saveExchangeRate(entity);

        return new ExchangeRateResponseDto(generatedId, currencyPairDto, exchangeRateRequestDto.getRate());
    }

    public ExchangeRateResponseDto selectExchangeRateByCurrenciesCodes(CurrencyPairDto currencyPairDto) throws SQLException {
        ExchangeRate exchangeRate = exchangeRateDao.selectExchangeRateByCodesIds(
                currencyPairDto.getBaseCurrency().getId(), currencyPairDto.getTargetCurrency().getId());

        return exchangeRate != null ?
                new ExchangeRateResponseDto(exchangeRate.getId(), currencyPairDto, exchangeRate.getRate())
                : null;
    }

    public boolean updateExchangeRate(BigDecimal rate, CurrencyPairDto currencyPairDto) throws SQLException {
        return exchangeRateDao.updateExchangeRate(
                rate, currencyPairDto.getBaseCurrency().getId(), currencyPairDto.getTargetCurrency().getId());
    }

    public Optional<BigDecimal> parseBigDecimal(String rate) {
        if (isNotBlank(rate)) {
            try {
                return Optional.of(new BigDecimal(rate));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    public boolean isValidParameters(String baseCurrencyCode, String targetCurrencyCode,
                                     Optional<BigDecimal> rateValue) {
        return isNotBlank(baseCurrencyCode) && isNotBlank(targetCurrencyCode) && rateValue.isPresent();
    }

    public boolean isValidCurrencies(CurrencyResponseDto baseCurrencyResponseDto,
                                     CurrencyResponseDto targetCurrencyResponseDto) {
        return baseCurrencyResponseDto != null && targetCurrencyResponseDto != null;
    }

    public boolean isCodesValid(String codes) {
        return codes != null && codes.length() == CURRENCY_CODES_LENGTH;
    }

    public Map<String, String> parseFormData(String body) {
        Map<String, String> params = new HashMap<>();

        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8);
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                params.put(key, value);
            }
        }

        return params;
    }

    public ExchangeRateResponseDto getExchangeRate(CurrencyPairDto currencyPairDto) throws SQLException {
        ExchangeRate exchangeRate = exchangeRateDao.selectExchangeRateByCodesIds(
                currencyPairDto.getBaseCurrency().getId(), currencyPairDto.getTargetCurrency().getId());
        ExchangeRateResponseDto exchangeRateResponseDto;

        if (exchangeRate == null) {
            exchangeRate = exchangeRateDao.selectExchangeRateByCodesIds(
                    currencyPairDto.getTargetCurrency().getId(), currencyPairDto.getBaseCurrency().getId());
            if (exchangeRate == null) {
                BigDecimal rateB = exchangeRateDao.selectRateByUsdAndTargetId(currencyPairDto.getTargetCurrency().getId());
                BigDecimal rateA = exchangeRateDao.selectRateByUsdAndTargetId(currencyPairDto.getBaseCurrency().getId());
                exchangeRateResponseDto = new ExchangeRateResponseDto(currencyPairDto,
                        rateB.divide(rateA, 2, RoundingMode.HALF_UP));
            } else {
                exchangeRateResponseDto = new ExchangeRateResponseDto(currencyPairDto,
                        new BigDecimal(1).divide(exchangeRate.getRate(), 2, RoundingMode.HALF_UP));
            }
        } else {
            exchangeRateResponseDto = new ExchangeRateResponseDto(currencyPairDto, exchangeRate.getRate());
        }

        return exchangeRateResponseDto;
    }
}
