package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyPairDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.rest.base.BaseExchangeRateServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateItemServlet extends BaseExchangeRateServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String codes = extractCurrencyCodeFromPath(request.getPathInfo());

            if (!isCodesValid(codes)) {
                sendErrorResponse(response, CURRENCY_CODES_MISSING_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyPairDto currencyPairDto = getCurrencyPairDto(response, codes);

            if (currencyPairDto == null) {
                return;
            }

            ExchangeRateResponseDto exchangeRateResponseDto =
                    exchangeRateService.selectExchangeRateByCurrenciesCodes(currencyPairDto);

            if (exchangeRateResponseDto == null) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            sendJsonResponse(response, exchangeRateResponseDto, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String codes = extractCurrencyCodeFromPath(request.getPathInfo());

            if (!isCodesValid(codes)) {
                sendErrorResponse(response, CURRENCY_CODES_MISSING_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            Map<String, String> formData = parseFormData(request);
            String rate = formData.get("rate");
            Optional<BigDecimal> rateValue = parseBigDecimal(rate);

            if (rateValue.isEmpty()) {
                sendErrorResponse(response, MISSING_FIELD_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyPairDto currencyPairDto = getCurrencyPairDto(response, codes);

            if (currencyPairDto == null) {
                return;
            }

            boolean updated = exchangeRateService.updateExchangeRate(rateValue.get(), currencyPairDto);

            if (!updated) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ExchangeRateResponseDto exchangeRateResponseDto =
                    exchangeRateService.selectExchangeRateByCurrenciesCodes(currencyPairDto);

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
