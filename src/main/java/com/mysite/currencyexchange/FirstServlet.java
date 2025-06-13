package com.mysite.currencyexchange;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.mapper.CurrencyMapper;
import com.mysite.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/first")
public class FirstServlet extends HttpServlet {

    private CurrencyService currencyService;
    private CurrencyDao currencyDao;
    private CurrencyMapper currencyMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyDao = new CurrencyDao();
        currencyMapper = CurrencyMapper.INSTANCE;
        currencyService = new CurrencyService(currencyDao, currencyMapper);
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var currencies = currencyService.selectAllCurrencies();

        String json = new Gson().toJson(currencies);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

//        super.doGet(req, resp);
    }
}
