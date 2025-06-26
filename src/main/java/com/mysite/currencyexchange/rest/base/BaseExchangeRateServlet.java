package com.mysite.currencyexchange.rest.base;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.dao.ExchangeRateDao;
import com.mysite.currencyexchange.mapper.CurrencyMapper;
import com.mysite.currencyexchange.mapper.ExchangeRateMapper;
import com.mysite.currencyexchange.service.CurrencyService;
import com.mysite.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;

public class BaseExchangeRateServlet extends BaseServlet {
    protected ExchangeRateService exchangeRateService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.INSTANCE;

        CurrencyDao currencyDao = new CurrencyDao();
        CurrencyMapper currencyMapper = CurrencyMapper.INSTANCE;
        CurrencyService currencyService = new CurrencyService(currencyDao, currencyMapper);

        exchangeRateService = new ExchangeRateService(exchangeRateDao, exchangeRateMapper, currencyService);
        gson = new Gson();

        super.init(config);
    }
}
