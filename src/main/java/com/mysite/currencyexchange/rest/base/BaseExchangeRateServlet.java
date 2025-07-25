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

    protected static final String CURRENCY_PAIR_ERROR =
            "One (or both) currencies from a currency pair do not exist in the database";
    protected static final String RATE_NOT_FOUND_ERROR = "The exchange rate for the pair was not found";
    protected static final String CURRENCY_CODES_MISSING_ERROR = "The currency codes of the pair are missing in the address";
    protected static final String CURRENCY_PAIR_EXISTS_ERROR = "A currency pair with this code already exists";

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
