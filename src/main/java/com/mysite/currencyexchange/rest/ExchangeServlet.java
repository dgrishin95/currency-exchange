package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyPairDto;
import com.mysite.currencyexchange.dto.ExchangeDto;
import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.rest.base.BaseExchangeRateServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseExchangeRateServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String baseCurrencyCode = request.getParameter("from");
            String targetCurrencyCode = request.getParameter("to");
            String amount = request.getParameter("amount");

            Optional<BigDecimal> amountValue = exchangeRateService.parseBigDecimal(amount);

            if (!exchangeRateService.isValidParameters(baseCurrencyCode, targetCurrencyCode, amountValue)) {
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
                    exchangeRateService.getExchangeRate(currencyPairDto);

            ExchangeDto exchangeDto = new ExchangeDto(currencyPairDto, exchangeRateResponseDto.getRate(),
                    amountValue.get());

            sendJsonResponse(response, exchangeDto, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
