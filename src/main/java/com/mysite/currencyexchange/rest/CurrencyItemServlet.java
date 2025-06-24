package com.mysite.currencyexchange.rest;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.dto.CurrencyResponseDto;
import com.mysite.currencyexchange.mapper.CurrencyMapper;
import com.mysite.currencyexchange.service.CurrencyService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/currencies/*")
public class CurrencyItemServlet extends HttpServlet {

    private CurrencyService currencyService;
    private CurrencyDao currencyDao;
    private CurrencyMapper currencyMapper;
    private Gson gson;

    @Override
    public void init(ServletConfig config) throws ServletException {
        currencyDao = new CurrencyDao();
        currencyMapper = CurrencyMapper.INSTANCE;
        currencyService = new CurrencyService(currencyDao, currencyMapper);
        gson = new Gson();
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.trim().isEmpty()) {
                processMissingCode(resp);
            } else {
                String code = pathInfo.substring(1); // "EUR"
                CurrencyResponseDto currencyResponseDto = currencyService.selectCurrencyByCode(code);

                if (currencyResponseDto == null) {
                    processCodeNotFound(resp);
                } else {
                    processFoundCode(currencyResponseDto, resp);
                }
            }
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = Map.of("error", "Database error");
            resp.getWriter().write(gson.toJson(error));
        }
    }

    private void processMissingCode(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String, String> error = Map.of("error", "Currency code is required");
        resp.getWriter().write(gson.toJson(error));
    }

    private void processCodeNotFound(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        Map<String, String> error = Map.of("error", "Currency code is not found");
        resp.getWriter().write(gson.toJson(error));
    }

    private void processFoundCode(CurrencyResponseDto currencyResponseDto, HttpServletResponse resp) throws IOException {
        String json = gson.toJson(currencyResponseDto);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }
}
