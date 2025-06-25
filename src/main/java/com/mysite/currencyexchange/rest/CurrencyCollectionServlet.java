package com.mysite.currencyexchange.rest;

import com.mysite.currencyexchange.dto.CurrencyRequestDto;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.rest.base.BaseCurrencyServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrencyCollectionServlet extends BaseCurrencyServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            List<CurrencyResponseDto> currencies = currencyService.selectAllCurrencies();
            sendJsonResponse(response, currencies, HttpServletResponse.SC_OK);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String name = request.getParameter("name");
            String code = request.getParameter("code");
            String sign = request.getParameter("sign");

            CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, name, sign);

            if (!isValidCurrencyRequest(currencyRequestDto)) {
                sendErrorResponse(response, MISSING_FIELD_ERROR, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (currencyService.currencyExistsByCode(code)) {
                sendErrorResponse(response, CURRENCY_EXISTS_ERROR, HttpServletResponse.SC_CONFLICT);
                return;
            }

            CurrencyResponseDto currencyResponseDto = currencyService.saveCurrency(currencyRequestDto);
            sendJsonResponse(response, currencyResponseDto, HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            sendErrorResponse(response, DATABASE_ERROR, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isValidCurrencyRequest(CurrencyRequestDto dto) {
        return isNotBlank(dto.getName()) &&
                isNotBlank(dto.getCode()) &&
                isNotBlank(dto.getSign());
    }

    private boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
