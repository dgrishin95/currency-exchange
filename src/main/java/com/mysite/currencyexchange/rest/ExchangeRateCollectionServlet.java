package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateRequestDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.rest.base.BaseExchangeRateServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRateCollectionServlet extends BaseExchangeRateServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<ExchangeRateResponseDto> exchangeRates = exchangeRateService.selectAllExchangeRates();
            sendJsonResponse(response, exchangeRates, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String baseCurrencyCode = request.getParameter("baseCurrencyCode");
            String targetCurrencyCode = request.getParameter("targetCurrencyCode");
            String rate = request.getParameter("rate");

            Optional<BigDecimal> rateValue = parseBigDecimal(rate);

            // Отсутствует нужное поле формы - 400
            if (!isValidParameters(baseCurrencyCode, targetCurrencyCode, rateValue)) {
                sendErrorResponse(response, MISSING_FIELD_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            // TODO: Валютная пара с таким кодом уже существует - 409

            // Одна (или обе) валюта из валютной пары не существует в БД - 404
            CurrencyResponseDto baseCurrencyResponseDto = exchangeRateService.selectCurrencyByCode(baseCurrencyCode);
            CurrencyResponseDto targetCurrencyResponseDto = exchangeRateService.selectCurrencyByCode(targetCurrencyCode);

            if (!isValidCurrencies(baseCurrencyResponseDto, targetCurrencyResponseDto)) {
                sendErrorResponse(response, CURRENCY_PAIR_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(
                    baseCurrencyCode, targetCurrencyCode, rateValue.get());

            // Успех - 201
            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.saveCurrency(exchangeRateRequestDto,
                    baseCurrencyResponseDto, targetCurrencyResponseDto);
            sendJsonResponse(response, exchangeRateResponseDto, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidParameters(String baseCurrencyCode, String targetCurrencyCode,
                                      Optional<BigDecimal> rateValue) {
        return isNotBlank(baseCurrencyCode) && isNotBlank(targetCurrencyCode) && rateValue.isPresent();
    }

    private boolean isValidCurrencies(CurrencyResponseDto baseCurrencyResponseDto,
                                      CurrencyResponseDto targetCurrencyResponseDto) {
        return baseCurrencyResponseDto != null && targetCurrencyResponseDto != null;
    }

    private Optional<BigDecimal> parseBigDecimal(String rate) {
        if (isNotBlank(rate)) {
            try {
                return Optional.of(new BigDecimal(rate));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
