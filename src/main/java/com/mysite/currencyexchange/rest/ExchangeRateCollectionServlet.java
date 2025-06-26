package com.mysite.currencyexchange.rest;

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

            Optional<Integer> baseCurrencyId = parseInteger(baseCurrencyCode);
            Optional<Integer> targetCurrencyId = parseInteger(targetCurrencyCode);
            Optional<BigDecimal> rateValue = parseBigDecimal(rate);

            if (!isValidParameters(baseCurrencyId, targetCurrencyId, rateValue)) {
                sendErrorResponse(response, MISSING_FIELD_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            ExchangeRateRequestDto exchangeRateRequestDto = new ExchangeRateRequestDto(
                    baseCurrencyId.get(), targetCurrencyId.get(), rateValue.get());


        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidParameters(Optional<Integer> baseCurrencyId, Optional<Integer> targetCurrencyId,
                                      Optional<BigDecimal> rateValue) {
        return baseCurrencyId.isPresent() && targetCurrencyId.isPresent() && rateValue.isPresent();
    }

    private Optional<Integer> parseInteger(String id) {
        if (isNotBlank(id)) {
            try {
                return Optional.of(Integer.parseInt(id));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
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
