package com.mysite.currencyexchange.rest;

import com.google.gson.Gson;
import com.mysite.currencyexchange.dao.CurrencyDao;
import com.mysite.currencyexchange.dto.CurrencyRequestDto;
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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@WebServlet("/currencies")
public class CurrencyCollectionServlet extends HttpServlet {

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
        List<CurrencyResponseDto> currencies;
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            currencies = currencyService.selectAllCurrencies();
            String json = gson.toJson(currencies);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = Map.of("error", "Database error");
            resp.getWriter().write(gson.toJson(error));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");

            CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(code, name, sign);

            if (!processFormIsNotFull(name, code, sign, resp)) {
                CurrencyResponseDto currencyResponseDto = currencyService.selectCurrencyByCode(code);

                if (currencyResponseDto != null) {
                    processCodeIsAlreadyExist(resp);
                } else {
                    processSaveCurrency(currencyRequestDto, resp);
                }
            }

        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> error = Map.of("error", "Database error");
            resp.getWriter().write(gson.toJson(error));
        }
    }

    private void processSaveCurrency(CurrencyRequestDto currencyRequestDto, HttpServletResponse resp)
            throws SQLException, IOException {
        int id = currencyService.saveCurrency(currencyRequestDto);
        currencyRequestDto.setId(id);

        String json = gson.toJson(currencyRequestDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.getWriter().write(json);
    }

    private boolean processFormIsNotFull(String name, String code, String sign, HttpServletResponse resp)
            throws IOException {
        if (name == null || name.trim().isEmpty()
                || code == null || code.trim().isEmpty()
                || sign == null || sign.trim().isEmpty()
        ) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            Map<String, String> error = Map.of("error", "The required form field is missing");
            resp.getWriter().write(gson.toJson(error));

            return true;
        }

        return false;
    }

    private void processCodeIsAlreadyExist(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_CONFLICT);
        Map<String, String> error = Map.of("error", "The currency with this code already exists");
        resp.getWriter().write(gson.toJson(error));
    }
}
