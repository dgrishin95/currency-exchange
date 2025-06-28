package com.mysite.currencyexchange.service.base;

public class BaseService {

    public boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }

    public String extractCurrencyCodeFromPath(String pathInfo) {
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.trim().isEmpty()) {
            return null;
        }

        String code = pathInfo.substring(1);
        return code.trim().isEmpty() ? null : code.toUpperCase();
    }
}
