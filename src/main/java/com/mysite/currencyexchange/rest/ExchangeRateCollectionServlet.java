package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.ExchangeRateResponseDto;
import com.mysite.currencyexchange.rest.base.BaseExchangeRateServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRateCollectionServlet extends BaseExchangeRateServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<ExchangeRateResponseDto> exchangeRates = exchangeRateService.selectAllExchangeRates();
            sendJsonResponse(response, exchangeRates, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
