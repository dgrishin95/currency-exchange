package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.rest.base.BaseExchangeRateServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateItemServlet extends BaseExchangeRateServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String codes = extractCurrencyCodeFromPath(request.getPathInfo());

            if (codes == null || codes.length() != CURRENCY_CODES_LENGTH) {
                sendErrorResponse(response, CURRENCY_CODES_MISSING_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String baseCurrencyCode = codes.substring(0, CURRENCY_CODE_LENGTH);
            String targetCurrencyCode = codes.substring(CURRENCY_CODE_LENGTH, CURRENCY_CODES_LENGTH);

            CurrencyResponseDto baseCurrencyResponseDto = exchangeRateService.selectCurrencyByCode(baseCurrencyCode);
            CurrencyResponseDto targetCurrencyResponseDto = exchangeRateService.selectCurrencyByCode(targetCurrencyCode);

            if (!isValidCurrencies(baseCurrencyResponseDto, targetCurrencyResponseDto)) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ExchangeRateResponseDto exchangeRateResponseDto = exchangeRateService.selectExchangeRateByCurrenciesCodes(
                    baseCurrencyResponseDto, targetCurrencyResponseDto);

            if (exchangeRateResponseDto == null) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            sendJsonResponse(response, exchangeRateResponseDto, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
