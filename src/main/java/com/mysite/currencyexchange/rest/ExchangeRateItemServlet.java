package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyPairDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.rest.base.BaseExchangeRateServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/exchangeRate/*")
public class ExchangeRateItemServlet extends BaseExchangeRateServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String codes = exchangeRateService.extractCurrencyCodeFromPath(request.getPathInfo());

            if (!exchangeRateService.isCodesValid(codes)) {
                sendErrorResponse(response, CURRENCY_CODES_MISSING_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyPairDto currencyPairDto = exchangeRateService.getCurrencyPairDto(codes);

            if (!exchangeRateService.isCurrencyPairExists(currencyPairDto)) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
            }

            ExchangeRateResponseDto exchangeRateResponseDto =
                    exchangeRateService.selectExchangeRateByCurrenciesCodes(currencyPairDto);

            if (!exchangeRateService.isExchangeRateExists(exchangeRateResponseDto)) {
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
            String codes = exchangeRateService.extractCurrencyCodeFromPath(request.getPathInfo());

            if (!exchangeRateService.isCodesValid(codes)) {
                sendErrorResponse(response, CURRENCY_CODES_MISSING_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            String rate = getRateParameter(body);

            Optional<BigDecimal> rateValue = exchangeRateService.parseBigDecimal(rate);

            if (rateValue.isEmpty()) {
                sendErrorResponse(response, MISSING_FIELD_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyPairDto currencyPairDto = exchangeRateService.getCurrencyPairDto(codes);

            if (!exchangeRateService.isCurrencyPairExists(currencyPairDto)) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean updated = exchangeRateService.updateExchangeRate(rateValue.get(), currencyPairDto);

            if (!updated) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ExchangeRateResponseDto exchangeRateResponseDto =
                    exchangeRateService.selectExchangeRateByCurrenciesCodes(currencyPairDto);

            if (!exchangeRateService.isExchangeRateExists(exchangeRateResponseDto)) {
                sendErrorResponse(response, RATE_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            sendJsonResponse(response, exchangeRateResponseDto, HttpServletResponse.SC_OK);

        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String getRateParameter(String body) {
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

        return params.get("rate");
    }
}
