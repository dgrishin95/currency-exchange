package com.mysite.currencyexchange.rest.base;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.ExchangeRateDao;
import com.mysite.currencyexchange.mapper.ExchangeRateMapper;
import com.mysite.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class BaseExchangeRateServlet extends BaseServlet {
    protected ExchangeRateService exchangeRateService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;
        exchangeRateService = new ExchangeRateService(exchangeRateDao, exchangeRateMapper);
        gson = new Gson();
        super.init(config);
    }
}
