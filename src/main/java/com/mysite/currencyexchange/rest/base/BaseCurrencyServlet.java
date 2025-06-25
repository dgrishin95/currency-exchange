package com.mysite.currencyexchange.rest.base;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.mapper.CurrencyMapper;
import com.mysite.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class BaseCurrencyServlet extends BaseServlet {
    protected CurrencyService currencyService;

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
}
