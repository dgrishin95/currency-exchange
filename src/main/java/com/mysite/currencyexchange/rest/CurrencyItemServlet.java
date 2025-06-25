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
            String code = extractCurrencyCodeFromPath(req.getPathInfo());

            if (code == null) {
                sendErrorResponse(resp, CURRENCY_CODE_REQUIRED_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyResponseDto currencyResponseDto = currencyService.selectCurrencyByCode(code);

            if (currencyResponseDto == null) {
                sendErrorResponse(resp, CURRENCY_NOT_FOUND_ERROR, HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            sendJsonResponse(resp, currencyResponseDto, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(resp, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private String extractCurrencyCodeFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.trim().isEmpty()) {
            return null;
        }

        String code = pathInfo.substring(1);
        return code.trim().isEmpty() ? null : code.toUpperCase();
    }
}
