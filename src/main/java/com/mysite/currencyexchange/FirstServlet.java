package com.mysite.currencyexchange;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.CurrencyDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/first")
public class FirstServlet extends HttpServlet {

    private CurrencyDao currencyDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyDao = new CurrencyDao();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var currencies = currencyDao.selectAllCurrencies();

        String json = new Gson().toJson(currencies);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);

//        super.doGet(req, resp);
    }
}
