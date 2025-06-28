package com.mysite.currencyexchange.service.base;

public class BaseService {

    protected boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
}
