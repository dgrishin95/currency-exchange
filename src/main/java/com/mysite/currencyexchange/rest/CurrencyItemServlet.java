package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.rest.base.BaseCurrencyServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyItemServlet extends BaseCurrencyServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String code = currencyService.extractCurrencyCodeFromPath(req.getPathInfo());

            if (!currencyService.isValidCode(code)) {
                sendErrorResponse(resp, CURRENCY_CODE_REQUIRED_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyResponseDto currencyResponseDto = currencyService.selectCurrencyByCode(code);

            if (!currencyService.isValidCurrencyResponseDto(currencyResponseDto)) {
                sendErrorResponse(resp, CURRENCY_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            sendJsonResponse(resp, currencyResponseDto, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(resp, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
