package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyPairDto;
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

            Optional<BigDecimal> rateValue = exchangeRateService.parseBigDecimal(rate);

            if (!exchangeRateService.isValidParameters(baseCurrencyCode, targetCurrencyCode, rateValue)) {
                sendErrorResponse(response, MISSING_FIELD_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyPairDto currencyPairDto =
                    exchangeRateService.getCurrencyPairDto(baseCurrencyCode, targetCurrencyCode);

            if (!exchangeRateService.isCurrencyPairExists(currencyPairDto)) {
                sendErrorResponse(response, CURRENCY_PAIR_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ExchangeRateResponseDto exchangeRateResponseDto =
                    exchangeRateService.selectExchangeRateByCurrenciesCodes(currencyPairDto);

            if (exchangeRateService.isExchangeRateExists(exchangeRateResponseDto)) {
                sendErrorResponse(response, CURRENCY_PAIR_EXISTS_ERROR, HttpServletResponse.SC_CONFLICT);
                return;
            }

            ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(
                    baseCurrencyCode, targetCurrencyCode, rateValue.get());

            exchangeRateResponseDto = exchangeRateService.saveExchangeRate(exchangeRateRequestDto, currencyPairDto);
            sendJsonResponse(response, exchangeRateResponseDto, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
