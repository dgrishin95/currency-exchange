package com.mysite.currencyexchange.rest.base;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class BaseServlet extends HttpServlet {
    protected Gson gson;

    protected static final String DATABASE_ERROR = "Database error";

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
