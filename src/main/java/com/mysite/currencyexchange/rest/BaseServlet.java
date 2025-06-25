package com.mysite.currencyexchange.rest;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.mapper.CurrencyMapper;
import com.mysite.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class BaseServlet extends HttpServlet {
    protected CurrencyService currencyService;
    protected Gson gson;

    protected static final String DATABASE_ERROR = "Database error";
    protected static final String MISSING_FIELD_ERROR = "The required form field is missing";
    protected static final String CURRENCY_EXISTS_ERROR = "The currency with this code already exists";
    protected static final String CURRENCY_NOT_FOUND_ERROR = "Currency code is not found";
    protected static final String CURRENCY_CODE_REQUIRED_ERROR = "Currency code is required";

    @Override
    public void init(ServletConfig config) throws ServletException {
        CurrencyDao currencyDao = new CurrencyDao();
        CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;
        currencyService = new CurrencyService(currencyDao, currencyMapper);
        gson = new Gson();
        super.init(config);
    }

    protected void sendJsonResponse(HttpServletResponse response, Object data, int statusCode)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(gson.toJson(data));
    }

    protected void sendErrorResponse(HttpServletResponse response, String errorMessage, int statusCode)
            throws IOException {
        Map<String, String> error = Map.of("error", errorMessage);
        sendJsonResponse(response, error, statusCode);
    }
}
